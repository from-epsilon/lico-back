package com.epsilon.nagginggnome.domain.plan.repository

import com.epsilon.nagginggnome.domain.plan.entity.PlanSnapshot

interface PlanSnapshotRepository {
    fun save(entity: PlanSnapshot): PlanSnapshot

    fun findLatestByPlanId(planId: java.util.UUID): PlanSnapshot?
}
