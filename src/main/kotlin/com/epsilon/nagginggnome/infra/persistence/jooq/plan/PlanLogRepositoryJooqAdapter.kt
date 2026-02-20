package com.epsilon.nagginggnome.infra.persistence.jooq.plan

import com.epsilon.nagginggnome.domain.plan.repository.PlanLogRepository
import com.epsilon.nagginggnome.generated.jooq.tables.references.PLAN_LOGS
import org.jooq.DSLContext
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
class PlanLogRepositoryJooqAdapter(
    private val dsl: DSLContext
) : PlanLogRepository {

    override fun insertIfAbsent(planId: UUID, userId: UUID): Int {
        return dsl.insertInto(PLAN_LOGS)
            .set(PLAN_LOGS.PLAN_ID, planId)
            .set(PLAN_LOGS.USER_ID, userId)
            .onConflict(PLAN_LOGS.PLAN_ID)
            .doNothing()
            .execute()
    }
}
