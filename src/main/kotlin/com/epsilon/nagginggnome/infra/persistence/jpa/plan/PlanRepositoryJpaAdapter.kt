package com.epsilon.nagginggnome.infra.persistence.jpa.plan

import com.epsilon.nagginggnome.domain.plan.entity.Plan
import com.epsilon.nagginggnome.domain.plan.repository.PlanRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
class PlanRepositoryJpaAdapter(
    private val jpa: JpaPlanRepository
) : PlanRepository {

    override fun findByIdAndUserId(planId: UUID, userId: UUID): Plan? {
        return jpa.findByIdAndUserId(planId, userId)
    }

    override fun save(plan: Plan): Plan {
        return jpa.save(plan)
    }
}


