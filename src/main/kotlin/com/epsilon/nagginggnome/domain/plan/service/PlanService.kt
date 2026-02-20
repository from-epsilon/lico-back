package com.epsilon.nagginggnome.domain.plan.service

import com.epsilon.nagginggnome.domain.llm.service.LlmJobService
import com.epsilon.nagginggnome.domain.plan.constant.PlanStatus
import com.epsilon.nagginggnome.domain.plan.dto.request.PlanCreateRequest
import com.epsilon.nagginggnome.domain.plan.dto.request.PlanUpdateRequest
import com.epsilon.nagginggnome.domain.plan.dto.response.PlanUpsertResponse
import com.epsilon.nagginggnome.domain.plan.entity.Plan
import com.epsilon.nagginggnome.domain.plan.entity.PlanSnapshot
import com.epsilon.nagginggnome.domain.plan.repository.PlanLogRepository
import com.epsilon.nagginggnome.domain.plan.repository.PlanRepository
import com.epsilon.nagginggnome.domain.plan.repository.PlanSnapshotRepository
import com.epsilon.nagginggnome.domain.push.service.PushJobService
import com.epsilon.nagginggnome.domain.user.repository.UserRepository
import com.epsilon.nagginggnome.domain.user.repository.UserSettingRepository
import com.epsilon.nagginggnome.global.constant.code.PlanErrorCode
import com.epsilon.nagginggnome.global.constant.code.UserErrorCode
import com.epsilon.nagginggnome.global.exception.ApiException
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Instant
import java.util.UUID

@Service
class PlanService(
    private val userRepository: UserRepository,
    private val planRepository: PlanRepository,
    private val planSnapshotRepository: PlanSnapshotRepository,
    private val planLogRepository: PlanLogRepository,
    private val userSettingRepository: UserSettingRepository,
    private val llmJobService: LlmJobService,
    private val pushJobService: PushJobService
) {

    /**
     * 플랜 생성
     */
    @Transactional
    fun createPlan(userId: UUID, req: PlanCreateRequest): PlanUpsertResponse {
        val user = userRepository.findById(
            userId = userId
        ) ?: throw ApiException(
            errorCode = UserErrorCode.USER_NOT_FOUND
        )

        val plan = req.plan
        val snapshot = req.snapshot

        // 멱등 처리
        planRepository.findByIdAndUserId(
            planId = plan.id,
            userId = userId
        )?.let { existing ->
            return PlanUpsertResponse(
                planId = existing.id,
                version = existing.currentVersion,
                snapshotAt = existing.currentSnapshotAt,
                status = existing.status,
            )
        }

        try {
            // 플랜 생성
            val newPlan = planRepository.save(
                plan = Plan(
                    planId = plan.id,
                    user = user,
                    action = plan.action,
                    purpose = plan.purpose,
                    motive = plan.motive,
                    memo = plan.memo,
                    dtstart = plan.dtstart,
                    rrule = plan.rrule,
                    remind = plan.remind,
                    leadTime = plan.leadTime,
                    currentVersion = plan.version,
                    currentSnapshotAt = plan.snapshotAt,
                    status = PlanStatus.ACTIVE,
                )
            )

            // 스냅샷 생성
            planSnapshotRepository.save(
                entity = PlanSnapshot(
                    planId = newPlan.id,
                    type = snapshot.type,
                    version = snapshot.version,
                    dataJson = snapshot.dataJson,
                    snapshotAt = snapshot.snapshotAt
                )
            )

            // 플랜 로그 초기 row 생성
            planLogRepository.insertIfAbsent(
                planId = newPlan.id,
                userId = userId
            )

            if (newPlan.remind && newPlan.status == PlanStatus.ACTIVE) {
                userSettingRepository.findById(
                    userId = userId
                )?.let { setting ->
                    llmJobService.enqueueReminderForPlan(
                        now = Instant.now(),
                        userId = userId,
                        planId = newPlan.id,
                        timezone = setting.timezone,
                        rrule = newPlan.rrule,
                        dtstart = newPlan.dtstart,
                        leadTime = newPlan.leadTime
                    )
                }
            }

            return PlanUpsertResponse(
                planId = newPlan.id,
                version = newPlan.currentVersion,
                snapshotAt = newPlan.currentSnapshotAt,
                status = newPlan.status,
            )
        } catch (_: DataIntegrityViolationException) {
            // 레이스 컨디션 대응
            planRepository.findByIdAndUserId(
                planId = plan.id,
                userId = userId
            )?.let { existing ->
                return PlanUpsertResponse(
                    planId = existing.id,
                    version = existing.currentVersion,
                    snapshotAt = existing.currentSnapshotAt,
                    status = existing.status,
                )
            }
            throw ApiException(
                errorCode = PlanErrorCode.PLAN_CREATE_FAILED
            )
        }
    }

    /**
     * 플랜 수정, 다음 스냅샷 생성
     */
    @Transactional
    fun updatePlan(userId: UUID, planId: UUID, req: PlanUpdateRequest): PlanUpsertResponse {
        val plan = planRepository.findByIdAndUserId(
            planId = planId,
            userId = userId
        ) ?: throw ApiException(
            errorCode = PlanErrorCode.PLAN_NOT_FOUND
        )

        val planPayload = req.plan
        val snapshot = req.snapshot
        val shouldRefreshReminder = shouldRefreshReminder(
            plan = plan,
            payload = planPayload
        )

        // 요청 버전 검증 및 멱등 처리
        val requestedVersion = planPayload.version
        val currentVersion = plan.currentVersion

        // 이미 지난 버전이면 적용 금지
        if (requestedVersion < currentVersion) {
            throw ApiException(
                errorCode = PlanErrorCode.PLAN_VERSION_STALE
            )
        }

        // 멱등 재시도
        if (requestedVersion == currentVersion) {
            return PlanUpsertResponse(
                planId = plan.id,
                version = plan.currentVersion,
                snapshotAt = plan.currentSnapshotAt,
                status = plan.status,
            )
        }

        // "현재 + 1"이 아니면 누락/순서 꼬임으로 판단
        // expectedVersion(currentVersion + 1)부터 재전송하도록 유도
        if (requestedVersion != currentVersion + 1) {
            throw ApiException(
                errorCode = PlanErrorCode.PLAN_VERSION_GAP
            )
        }

        // 플랜 수정
        plan.patch(
            action = planPayload.action,
            purpose = planPayload.purpose,
            motive = planPayload.motive,
            memo = planPayload.memo,
            dtstart = planPayload.dtstart,
            rrule = planPayload.rrule,
            remind = planPayload.remind,
            leadTime = planPayload.leadTime,
            status = planPayload.status,
            nextVersion = requestedVersion,
            nextSnapshotAt = planPayload.snapshotAt
        )

        try {
            // 스냅샷 생성
            planSnapshotRepository.save(
                entity = PlanSnapshot(
                    planId = planId,
                    type = snapshot.type,
                    version = requestedVersion,
                    dataJson = snapshot.dataJson,
                    snapshotAt = snapshot.snapshotAt
                )
            )
        } catch (_: DataIntegrityViolationException) {
            // 멱등 성공 처리
        }

        if (shouldRefreshReminder) {
            pushJobService.deleteScheduledReminders(
                planId = plan.id,
                from = Instant.now()
            )
            if (plan.remind && plan.status == PlanStatus.ACTIVE) {
                userSettingRepository.findById(
                    userId = userId
                )?.let { setting ->
                    llmJobService.enqueueReminderForPlan(
                        now = Instant.now(),
                        userId = userId,
                        planId = plan.id,
                        timezone = setting.timezone,
                        rrule = plan.rrule,
                        dtstart = plan.dtstart,
                        leadTime = plan.leadTime
                    )
                }
            }
        }

        return PlanUpsertResponse(
            planId = plan.id,
            version = plan.currentVersion,
            snapshotAt = plan.currentSnapshotAt,
            status = plan.status,
        )
    }

    @Transactional
    fun deletePlan(userId: UUID, planId: UUID) {
        // 기존 플랜 조회
        val plan = planRepository.findByIdAndUserId(
            planId = planId,
            userId = userId
        ) ?: throw ApiException(
            errorCode = PlanErrorCode.PLAN_NOT_FOUND
        )

        // 플랜 삭제 멱등 처리
        plan.takeIf { !it.isDeleted() }?.softDelete()
        pushJobService.deleteScheduledReminders(
            planId = plan.id,
            from = Instant.now()
        )
    }

    private fun shouldRefreshReminder(
        plan: Plan,
        payload: PlanUpdateRequest.PlanPayload
    ): Boolean {
        val actionChanged = payload.action?.let { it != plan.action } ?: false
        val rruleChanged = payload.rrule?.let { it != plan.rrule } ?: false
        val leadTimeChanged = payload.leadTime?.let { it != plan.leadTime } ?: false
        val remindChanged = payload.remind?.let { it != plan.remind } ?: false
        val statusChanged = payload.status?.let { it != plan.status } ?: false
        return actionChanged || rruleChanged || leadTimeChanged || remindChanged || statusChanged
    }
}
