package com.epsilon.nagginggnome.domain.push.service

import com.epsilon.nagginggnome.domain.push.constant.PushJobStatus
import com.epsilon.nagginggnome.domain.push.repository.FcmTokenRepository
import com.epsilon.nagginggnome.domain.push.repository.PushJobRepository
import com.epsilon.nagginggnome.domain.push.repository.model.PushJobProcessingModel
import com.epsilon.nagginggnome.global.constant.code.PushJobErrorCode
import com.epsilon.nagginggnome.global.exception.ApiException
import com.epsilon.nagginggnome.infra.fcm.converter.FcmDataJsonConverter
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import tools.jackson.databind.ObjectMapper
import java.time.Instant

@Service
class PushJobBatchService(
    private val pushJobRepository: PushJobRepository,
    private val fcmTokenRepository: FcmTokenRepository,
    private val fcmPushService: FcmPushService,
    private val objectMapper: ObjectMapper
) {

    /**
     * 처리 가능한 푸시 작업 실행
     */
    fun processReadyJobs(now: Instant, limit: Int) {

        // 처리 대상 조회 + RUNNING 전환
        val jobs: List<PushJobProcessingModel> = lockAndMarkRunning(now, limit)
        if (jobs.isEmpty()) {
            return
        }

        // FCM 전송은 트랜잭션 밖에서 수행
        val doneIds = mutableListOf<Long>()
        val failedIds = mutableListOf<Long>()

        jobs.forEach { job ->
            runCatching {
                val fcmToken = fcmTokenRepository.findByUserId(job.userId)
                    ?: throw ApiException(PushJobErrorCode.FCM_TOKEN_NOT_FOUND)
                val data = FcmDataJsonConverter.toFcmDataMap(objectMapper, job.dataJson)

                fcmPushService.sendToToken(
                    token = fcmToken.token,
                    title = job.title,
                    body = job.body,
                    data = data
                )
            }.onSuccess {
                doneIds.add(job.id)
            }.onFailure {
                failedIds.add(job.id)
            }
        }

        // 결과 상태 반영
        updateStatuses(doneIds, failedIds)
    }

    /**
     * READY 작업을 락으로 조회하고 RUNNING으로 전환
     * - 같은 트랜잭션에서 수행되어야 원자성이 보장
     */
    @Transactional
    fun lockAndMarkRunning(now: Instant, limit: Int): List<PushJobProcessingModel> {
        val locked = pushJobRepository.lockNextReadyJobs(now, limit)
        if (locked.isEmpty()) {
            return emptyList()
        }

        // 락으로 가져온 ID만 RUNNING으로 전환
        pushJobRepository.updateStatus(
            ids = locked.map { it.id },
            status = PushJobStatus.RUNNING
        )
        return locked
    }

    /**
     * 처리 결과를 DONE/FAILED로 반영
     */
    @Transactional
    fun updateStatuses(doneIds: List<Long>, failedIds: List<Long>) {
        if (doneIds.isNotEmpty()) {
            pushJobRepository.updateStatus(doneIds, PushJobStatus.DONE)
        }
        if (failedIds.isNotEmpty()) {
            pushJobRepository.updateStatus(failedIds, PushJobStatus.FAILED)
        }
    }
}
