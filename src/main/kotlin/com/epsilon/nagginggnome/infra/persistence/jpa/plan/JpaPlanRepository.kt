package com.epsilon.nagginggnome.infra.persistence.jpa.plan

import com.epsilon.nagginggnome.domain.plan.entity.Plan
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface JpaPlanRepository : JpaRepository<Plan, UUID> {

    fun findByIdAndUserId(planId: UUID, userId: UUID): Plan?
}
