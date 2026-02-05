package com.epsilon.nagginggnome.domain.llm.dto.response

import com.epsilon.nagginggnome.domain.push.constant.PushType
import java.time.Instant
import java.util.UUID

/**
 * PUSH_BATCH 작업 output_json
 */
data class PushBatchJobOutput(
    val userId: UUID,
    val timeWindow: TimeWindow,
    val messages: List<Message>
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
        val pushType: PushType
    )
}
