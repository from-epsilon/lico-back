package com.epsilon.nagginggnome.domain.plan.dto.response

import java.time.Instant

data class PlanListItemResponse(
    val planId: Long,
    val currentVersion: Int,
    val currentSnapshotId: Long,
    val action: String,
    val rrule: String,
    val dtstart: Instant,
)
