package com.epsilon.nagginggnome.domain.plan.dto.response

import com.epsilon.nagginggnome.domain.plan.constant.PlanStatus
import java.time.Instant
import java.util.*

/**
 * 플랜 생성 응답 DTO
 */
data class PlanCreateResponse(
    val planId: UUID,
    val currentVersion: Int,
    val status: PlanStatus,
    val createdAt: Instant
)
