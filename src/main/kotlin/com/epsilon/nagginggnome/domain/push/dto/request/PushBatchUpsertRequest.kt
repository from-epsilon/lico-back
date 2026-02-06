package com.epsilon.nagginggnome.domain.push.dto.request

import com.epsilon.nagginggnome.domain.message.constant.MessageType
import java.time.Instant
import java.util.UUID

/**
 * LLM 푸시 배치 업서트 요청 DTO
 */
data class PushBatchUpsertRequest(
    val userId: UUID,
    val timeWindow: TimeWindow,
    val pushes: List<Message>,
    val meta: Meta?
) {

    data class TimeWindow(
        val from: Instant,
        val to: Instant
    )

    data class Message(
        val scheduledAt: Instant,
        val title: String,
        val body: String,
        val intent: String?,
        val type: MessageType,
        val planId: UUID?
    )

    data class Meta(
        val model: String,
        val tokenUsage: Int,
        val generatedAt: Instant
    )
}
