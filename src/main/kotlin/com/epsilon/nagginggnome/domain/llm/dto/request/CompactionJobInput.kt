package com.epsilon.nagginggnome.domain.llm.dto.request

import com.epsilon.nagginggnome.domain.llm.constant.LlmJobTargetType
import java.time.Instant
import java.util.UUID

/**
 * COMPACTION 작업 input_json
 */
data class CompactionJobInput(
    val planId: UUID,
    val compactionTarget: List<CompactionTargetItem>
) {
    data class CompactionTargetItem(
        val type: LlmJobTargetType,
        val timestamp: Instant,
        val content: Any
    )

    data class GnomeMessageContent(
        val title: String,
        val body: String
    )

    data class UserMessageContent(
        val replyTo: ReplyTo,
        val body: String
    )

    data class ReplyTo(
        val title: String,
        val body: String
    )

    data class UpdateContent(
        val fields: List<UpdateField>
    )

    data class UpdateField(
        val field: String,
        val before: Any?,
        val after: Any?
    )
}
