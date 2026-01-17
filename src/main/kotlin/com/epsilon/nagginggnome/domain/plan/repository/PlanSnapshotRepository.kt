package com.epsilon.nagginggnome.domain.plan.repository

import com.epsilon.nagginggnome.domain.plan.entity.PlanSnapshot
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface PlanSnapshotRepository : JpaRepository<PlanSnapshot, Long> {
}
