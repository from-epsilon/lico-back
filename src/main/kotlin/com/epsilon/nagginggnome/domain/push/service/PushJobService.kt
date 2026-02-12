package com.epsilon.nagginggnome.domain.push.service

import com.epsilon.nagginggnome.domain.llm.constant.LlmMetaData
import com.epsilon.nagginggnome.domain.push.constant.PushJobStatus
import com.epsilon.nagginggnome.domain.push.dto.request.PushBatchUpsertRequest
import com.epsilon.nagginggnome.domain.push.repository.PushJobRepository
import com.epsilon.nagginggnome.domain.push.repository.model.PushJobCreateModel
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import tools.jackson.databind.ObjectMapper
import java.time.Instant
import java.util.UUID

@Service
class PushJobService(
    private val pushJobRepository: PushJobRepository,
    private val objectMapper: ObjectMapper
) {

    /**
     * 특정 플랜의 예정된 알림 삭제 (발송되지 않은 것만)
     */
    @Transactional
    fun deleteScheduledReminders(planId: UUID, from: Instant): Int {
        return pushJobRepository.deleteReadyByPlanFrom(
            planId = planId,
            from = from
        )
    }

        val createModels = req.pushes.map { message ->
            PushJobCreateModel(
                userId = userId,
                planId = message.planId,
                type = message.type,
                title = message.title,
                body = message.body,
                dataJson = null,
                llmMetaJson = buildLlmMetaJson(
                    meta = req.meta,
                    intent = message.intent
                ),
                status = PushJobStatus.READY,
                scheduledAt = message.scheduledAt
            )
        }

        pushJobRepository.insertPushJob(
            userId = userId,
            models = createModels
        )
    }

    private fun buildLlmMetaJson(
        meta: PushBatchUpsertRequest.Meta?,
        intent: String?
    ): String? {
        val payload = buildMap<String, Any> {
            meta?.let {
                put(LlmMetaData.MODEL.key, it.model)
                put(LlmMetaData.TOKEN_USAGE.key, it.tokenUsage)
                put(LlmMetaData.GENERATED_AT.key, it.generatedAt)
            }
            intent?.let { put(LlmMetaData.INTENT.key, it) }
        }
        return payload.takeIf { it.isNotEmpty() }?.let(
            objectMapper::writeValueAsString
        )
    }
}
