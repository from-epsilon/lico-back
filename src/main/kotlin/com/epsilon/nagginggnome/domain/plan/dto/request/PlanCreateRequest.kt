package com.epsilon.nagginggnome.domain.plan.dto.request

import java.time.Instant
import java.util.*

/**
 * 플랜 생성 요청 DTO
 */
data class PlanCreateRequest(
    val plan: PlanPayload,
    val snapshot: SnapshotPayload
) {

    data class PlanPayload(
        val id: UUID,
        val action: String,
        val purpose: String?,
        val motive: String?,
        val memo: String?,
        val dtstart: Instant,
        val rrule: String,
        val remind: Boolean,
        val leadTime: Int?,
        val version: Int,
        val snapshotAt: Instant
    )

    data class SnapshotPayload(
        val id: UUID,
        val planId: UUID,
        val version: Int,
        val snapshotAt: Instant,
        val dataJson: Map<String, Any?>
    )
}
