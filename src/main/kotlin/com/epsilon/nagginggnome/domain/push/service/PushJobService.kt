package com.epsilon.nagginggnome.domain.push.service

import com.epsilon.nagginggnome.domain.llm.constant.LlmMetaData
import com.epsilon.nagginggnome.domain.llm.dto.response.AdditionalJobOutput
import com.epsilon.nagginggnome.domain.llm.dto.response.ReminderJobOutput
import com.epsilon.nagginggnome.domain.message.constant.MessageType
import com.epsilon.nagginggnome.domain.push.constant.PushJobStatus
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

    /**
     * LLM에서 생성한 REMINDER 반영
     */
    @Transactional
    fun insertReminder(output: ReminderJobOutput) {
        val createModel = PushJobCreateModel(
            userId = output.request.userId,
            planId = output.request.planId,
            type = MessageType.REMINDER,
            title = output.reminder.title,
            body = output.reminder.body,
            dataJson = null,
            llmMetaJson = buildLlmMetaJson(
                meta = output.meta,
                intent = output.reminder.intent
            ),
            status = PushJobStatus.READY,
            scheduledAt = output.request.scheduledAt
        )

        pushJobRepository.insertPushJob(
            userId = output.request.userId,
            models = listOf(createModel)
        )
    }

    /**
     * LLM에서 생성한 ADDITIONAL 반영
     */
    @Transactional
    fun insertAdditional(output: AdditionalJobOutput) {
        val createModel = PushJobCreateModel(
            userId = output.request.userId,
            planId = null,
            type = MessageType.ADDITIONAL,
            title = output.additional.title,
            body = output.additional.body,
            dataJson = buildAdditionalDataJson(
                score = output.additional.score
            ),
            llmMetaJson = buildLlmMetaJson(
                model = output.meta.model,
                tokenUsage = output.meta.tokenUsage,
                generatedAt = output.meta.generatedAt,
                intent = output.additional.intent
            ),
            status = PushJobStatus.READY,
            scheduledAt = output.additional.scheduledAt
        )

        pushJobRepository.insertPushJob(
            userId = output.request.userId,
            models = listOf(createModel)
        )
    }

    private fun buildLlmMetaJson(
        meta: ReminderJobOutput.Meta,
        intent: String?
    ): String? {
        val payload = buildMap<String, Any> {
            put(LlmMetaData.MODEL.key, meta.model)
            put(LlmMetaData.TOKEN_USAGE.key, meta.tokenUsage)
            put(LlmMetaData.GENERATED_AT.key, meta.generatedAt)
            intent?.let { put(LlmMetaData.INTENT.key, it) }
        }
        return payload.takeIf { it.isNotEmpty() }?.let(
            objectMapper::writeValueAsString
        )
    }

    private fun buildLlmMetaJson(
        model: String,
        tokenUsage: Int,
        generatedAt: Instant,
        intent: String?
    ): String? {
        val payload = buildMap<String, Any> {
            put(LlmMetaData.MODEL.key, model)
            put(LlmMetaData.TOKEN_USAGE.key, tokenUsage)
            put(LlmMetaData.GENERATED_AT.key, generatedAt)
            intent?.let { put(LlmMetaData.INTENT.key, it) }
        }
        return payload.takeIf { it.isNotEmpty() }?.let(
            objectMapper::writeValueAsString
        )
    }

    private fun buildAdditionalDataJson(score: Int): String {
        val payload = mapOf(
            "score" to score.toString()
        )
        return objectMapper.writeValueAsString(payload)
    }
}
