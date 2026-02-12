package com.epsilon.nagginggnome.domain.llm.dto.response

import java.time.Instant
import java.util.UUID

/**
 * REMINDER 작업 output_json
 */
data class ReminderJobOutput(
    val request: Request,
    val reminder: Reminder,
    val meta: Meta
) {
    data class Request(
        val userId: UUID,
        val planId: UUID,
        val scheduledAt: Instant
    )

    data class Reminder(
        val title: String,
        val body: String,
        val intent: String
    )

    data class Meta(
        val model: String,
        val tokenUsage: Int,
        val generatedAt: Instant
    )
}
