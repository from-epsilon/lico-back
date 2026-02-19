package com.epsilon.nagginggnome.domain.llm.dto.response

import com.epsilon.nagginggnome.domain.llm.constant.LlmJobTargetType
import java.time.Instant
import java.util.UUID

/**
 * COMPACTION 작업 output_json
 */
data class CompactionJobOutput(
    val request: Request,
    val compaction: String,
    val meta: Meta
) {
    data class Request(
        val planId: UUID,
        val compactionTarget: List<CompactionTargetItem>
    ) {
        data class CompactionTargetItem(
            val type: LlmJobTargetType,
            val timestamp: Instant,
            val content: Any
        )
    }

    data class Meta(
        val model: String,
        val tokenUsage: Int,
        val generatedAt: Instant
    )
}
