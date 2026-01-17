package com.epsilon.nagginggnome.domain.plan.service

import com.epsilon.nagginggnome.domain.plan.dto.request.PlanCreateRequest
import com.epsilon.nagginggnome.domain.plan.dto.response.PlanCreateResponse
import com.epsilon.nagginggnome.domain.plan.dto.response.PlanListItemResponse
import com.epsilon.nagginggnome.domain.plan.entity.Plan
import com.epsilon.nagginggnome.domain.plan.entity.PlanSnapshot
import com.epsilon.nagginggnome.domain.plan.repository.PlanRepository
import com.epsilon.nagginggnome.domain.plan.repository.PlanSnapshotRepository
import com.epsilon.nagginggnome.domain.user.repository.UserRepository
import com.epsilon.nagginggnome.global.constant.code.PlanErrorCode
import com.epsilon.nagginggnome.global.constant.code.UserErrorCode
import com.epsilon.nagginggnome.global.exception.ApiException
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class PlanService(
    private val userRepository: UserRepository,
    private val planRepository: PlanRepository,
    private val planSnapshotRepository: PlanSnapshotRepository
) {

    /**
     * 내 플랜 목록 조회
     */
    @Transactional(readOnly = true)
    fun getMyPlans(userId: UUID, pageable: Pageable): Page<PlanListItemResponse> {
        return planRepository.findMyPlans(userId, pageable)
    }

    /**
     * 플랜 생성
     */
    @Transactional
    fun createPlan(req: PlanCreateRequest, userId: UUID): PlanCreateResponse {
        val user = userRepository.findByIdOrNull(userId)
            ?: throw ApiException(UserErrorCode.USER_NOT_FOUND)

        val newPlan = planRepository.save(
            Plan(
                user = user
            )
        )
        val newPlanId = newPlan.id
            ?: throw ApiException(PlanErrorCode.PLAN_CREATE_FAILED)

        val newSnapshot = planSnapshotRepository.save(
            PlanSnapshot(
                plan = newPlan,
                version = 1,
                action = req.action,
                rrule = req.rrule,
                dtstart = req.dtstart,
                purpose = req.purpose,
                motive = req.motive,
                memo = req.memo
            )
        )
        val newSnapshotId = newSnapshot.id
            ?: throw ApiException(PlanErrorCode.PLAN_CREATE_FAILED)
        newPlan.pointToSnapshot(snapshotId = newSnapshotId, version = newSnapshot.version)

        return PlanCreateResponse(
            planId = newPlanId,
            snapshotId = newSnapshotId,
            version = newSnapshot.version
        )
    }
}
