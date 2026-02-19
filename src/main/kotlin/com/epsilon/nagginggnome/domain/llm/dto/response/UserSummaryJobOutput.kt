package com.epsilon.nagginggnome.domain.llm.dto.response

import java.time.Instant
import java.util.UUID

/**
 * USER_SUMMARY 작업 output_json
 */
data class UserSummaryJobOutput(
    val userId: UUID,
    val summaryJson: Map<String, Any?>?,
    val updatedAt: Instant,
    val meta: Meta?
) {
    data class Meta(
        val model: String,
        val tokenUsage: Int,
        val generatedAt: Instant
    )
}
