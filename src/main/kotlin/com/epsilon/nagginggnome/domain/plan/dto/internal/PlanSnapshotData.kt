package com.epsilon.nagginggnome.domain.plan.dto.internal

import com.epsilon.nagginggnome.domain.plan.constant.PlanStatus
import java.time.Instant

/**
 * PlanSnapshot.data_json에 저장할 스냅샷 DTO
 */
data class PlanSnapshotData(
    val action: String,
    val purpose: String?,
    val motive: String?,
    val memo: String?,
    val dtstart: Instant,
    val rrule: String,
    val remind: Boolean,
    val leadTime: Int?,
    val currentVersion: Int,
    val currentSnapshotAt: Instant,
    val status: PlanStatus,
)
