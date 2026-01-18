package com.epsilon.nagginggnome.domain.plan.dto.response

import java.time.Instant

/**
 * 플랜 상세 조회 응답 DTO
 */
data class PlanDetailResponse(
    val planId: Long,
    val currentVersion: Int,
    val currentSnapshotId: Long,
    val action: String,
    val rrule: String,
    val dtstart: Instant,
    val purpose: String?,
    val motive: String?,
    val memo: String?
)
