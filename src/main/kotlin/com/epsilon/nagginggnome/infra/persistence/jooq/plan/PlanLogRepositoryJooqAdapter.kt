package com.epsilon.nagginggnome.infra.persistence.jooq.plan

import com.epsilon.nagginggnome.domain.plan.repository.PlanLogRepository
import com.epsilon.nagginggnome.generated.jooq.tables.references.PLAN_LOGS
import org.jooq.DSLContext
import org.springframework.stereotype.Repository
import java.util.UUID
import org.jooq.JSONB
import org.jooq.impl.DSL

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

    override fun appendRecentLog(planId: UUID, logJson: String): Int {
        val appended = DSL.field(
            "array_append({0}, {1})",
            PLAN_LOGS.RECENT_LOGS.dataType,
            PLAN_LOGS.RECENT_LOGS,
            JSONB.valueOf(logJson)
        )
        return dsl.update(PLAN_LOGS)
            .set(PLAN_LOGS.RECENT_LOGS, appended)
            .where(PLAN_LOGS.PLAN_ID.eq(planId))
            .execute()
    }
}
