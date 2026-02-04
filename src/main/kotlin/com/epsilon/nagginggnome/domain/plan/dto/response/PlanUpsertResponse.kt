package com.epsilon.nagginggnome.domain.plan.dto.response

import com.epsilon.nagginggnome.domain.plan.constant.PlanStatus
import java.time.Instant
import java.util.UUID

/**
 * 플랜 생성, 수정 응답 DTO
 */
data class PlanUpsertResponse(
    val planId: UUID,
    val version: Int,
    val snapshotAt: Instant,
    val status: PlanStatus,
)
