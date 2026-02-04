package com.epsilon.nagginggnome.domain.plan.repository

import com.epsilon.nagginggnome.domain.plan.entity.Plan
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface PlanRepository : JpaRepository<Plan, UUID> {

    /**
     * 플랜 조회(소유자 검증 포함)
     * - deletedAt 필터가 없으므로, 이미 삭제된 플랜도 조회될 수 있음
     */
    fun findByIdAndUserId(planId: UUID, userId: UUID): Plan?
}
