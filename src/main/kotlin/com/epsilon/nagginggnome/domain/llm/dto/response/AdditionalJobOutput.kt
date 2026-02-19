package com.epsilon.nagginggnome.domain.llm.dto.response

import java.time.Instant
import java.util.UUID

/**
 * ADDITIONAL 작업 output_json
 */
data class AdditionalJobOutput(
    val request: Request,
    val additional: Additional,
    val meta: Meta
) {
    data class Request(
        val userId: UUID,
        val timeWindow: TimeWindow
    ) {
        data class TimeWindow(
            val start: Instant,
            val end: Instant
        )
    }

    data class Additional(
        val title: String,
        val body: String,
        val intent: String,
        val scheduledAt: Instant,
        val score: Int
    )

    data class Meta(
        val model: String,
        val tokenUsage: Int,
        val generatedAt: Instant
    )
}
