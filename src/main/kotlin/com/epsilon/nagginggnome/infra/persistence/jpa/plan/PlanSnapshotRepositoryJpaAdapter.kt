package com.epsilon.nagginggnome.infra.persistence.jpa.plan

import com.epsilon.nagginggnome.domain.plan.entity.PlanSnapshot
import com.epsilon.nagginggnome.domain.plan.repository.PlanSnapshotRepository
import org.springframework.stereotype.Repository

@Repository
class PlanSnapshotRepositoryJpaAdapter(
    private val jpa: JpaPlanSnapshotRepository
) : PlanSnapshotRepository {

    override fun save(entity: PlanSnapshot): PlanSnapshot {
        return jpa.save(entity)
    }
}
