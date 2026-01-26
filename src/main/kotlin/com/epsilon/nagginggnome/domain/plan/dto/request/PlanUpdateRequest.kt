package com.epsilon.nagginggnome.domain.plan.dto.request

import java.time.Instant

/**
 * 플랜 수정 요청 DTO
 */
data class PlanUpdateRequest(
    val action: String?,
    val purpose: String?,
    val motive: String?,
    val memo: String?,
    val dtstart: Instant?,
    val rrule: String?,
    val remind: Boolean?,
    val leadTime: Int?,
    val version: Int,
    val snapshotAt: Instant
)
