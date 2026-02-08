package com.epsilon.nagginggnome.infra.persistence.jpa.plan

import com.epsilon.nagginggnome.domain.plan.entity.PlanSnapshot
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface JpaPlanSnapshotRepository : JpaRepository<PlanSnapshot, UUID> {
    fun findTopByPlanIdOrderByVersionDesc(planId: UUID): PlanSnapshot?
}
