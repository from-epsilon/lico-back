package com.epsilon.nagginggnome.domain.plan.dto.response

import java.time.Instant
import java.util.*

/**
 * 플랜 수정 응답 DTO
 */
data class PlanUpdateResponse(
    val planId: UUID,
    val currentVersion: Int,
    val updatedAt: Instant
)
