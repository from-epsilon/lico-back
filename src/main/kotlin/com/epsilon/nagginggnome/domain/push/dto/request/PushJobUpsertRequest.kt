package com.epsilon.nagginggnome.domain.push.dto.request

import com.fasterxml.jackson.annotation.JsonProperty
import java.time.Instant
import java.util.UUID

/**
 * 알림 스케줄 일괄 업로드 요청 DTO
 */
data class PushJobUpsertRequest(
    val batchId: UUID,
    val range: Range,
    val notices: List<Notice>
) {

    data class Range(
        val from: Instant,
        val to: Instant
    )

    data class Notice(
        @JsonProperty("scheduledAtUtc")
        val scheduledAt: Instant,
        val type: String,
        val planId: UUID?
    )
}
