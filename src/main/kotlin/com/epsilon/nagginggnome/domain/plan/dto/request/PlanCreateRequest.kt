package com.epsilon.nagginggnome.domain.plan.dto.request

import java.time.Instant
import java.util.*

/**
 * 플랜 생성 요청 DTO
 */
data class PlanCreateRequest(
    val planId: UUID,
    val action: String,
    val purpose: String?,
    val motive: String?,
    val memo: String?,
    val dtstart: Instant,
    val rrule: String,
    val remind: Boolean,
    val leadTime: Int?,
    val version: Int,
    val createdAt: Instant
)
