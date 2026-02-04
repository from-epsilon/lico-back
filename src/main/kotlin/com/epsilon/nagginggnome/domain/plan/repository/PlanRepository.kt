package com.epsilon.nagginggnome.domain.plan.repository

import com.epsilon.nagginggnome.domain.plan.entity.Plan
import java.util.UUID

/**
 * 플랜 저장소 인터페이스
 */
interface PlanRepository {

    /**
     * 플랜 조회(소유자 검증 포함)
     * - deletedAt 필터가 없으므로, 이미 삭제된 플랜도 조회될 수 있음
     */
    fun findByIdAndUserId(planId: UUID, userId: UUID): Plan?

    /**
     * 플랜 저장
     */
    fun save(plan: Plan): Plan
}


