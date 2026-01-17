package com.epsilon.nagginggnome.domain.plan.dto.response

/**
 * 플랜 수정 응답 DTO
 */
data class PlanUpdateResponse(
    val planId: Long,
    val snapshotId: Long,
    val version: Int
)
