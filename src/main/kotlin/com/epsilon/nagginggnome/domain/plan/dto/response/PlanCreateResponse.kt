package com.epsilon.nagginggnome.domain.plan.dto.response

/**
 * 플랜 생성 응답 DTO
 */
data class PlanCreateResponse(
    val planId: Long,
    val snapshotId: Long,
    val snapshotVersion: Int
)
