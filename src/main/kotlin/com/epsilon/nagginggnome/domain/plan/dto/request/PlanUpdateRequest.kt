package com.epsilon.nagginggnome.domain.plan.dto.request

import java.time.Instant
import java.util.*
import com.epsilon.nagginggnome.domain.plan.constant.PlanStatus

/**
 * 플랜 수정 요청 DTO
 */
data class PlanUpdateRequest(
    val plan: PlanPayload,
    val snapshot: SnapshotPayload
) {

    data class PlanPayload(
        val action: String?,
        val purpose: String?,
        val motive: String?,
        val memo: String?,
        val dtstart: Instant?,
        val rrule: String?,
        val remind: Boolean?,
        val leadTime: Int?,
        val status: PlanStatus?,
        val version: Int,
        val snapshotAt: Instant
    )

    data class SnapshotPayload(
        val id: UUID,
        val planId: UUID,
        val type: String,
        val version: Int,
        val snapshotAt: Instant,
        val dataJson: Map<String, Any?>
    )
}
