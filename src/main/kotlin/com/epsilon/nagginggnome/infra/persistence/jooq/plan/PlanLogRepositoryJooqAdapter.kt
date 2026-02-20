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

    override fun findRecentLogs(planId: UUID): List<String> {
        return dsl
            .select(PLAN_LOGS.RECENT_LOGS)
            .from(PLAN_LOGS)
            .where(PLAN_LOGS.PLAN_ID.eq(planId))
            .fetchOne { rec ->
                rec.get(PLAN_LOGS.RECENT_LOGS)
                    ?.map { it.data() }
                    ?: emptyList()
            } ?: emptyList()
    }

    override fun removeOldestRecentLogs(planId: UUID, count: Int): Int {
        if (count <= 0) {
            return 0
        }
        val trimmed = DSL.field(
            "CASE WHEN array_length({0}, 1) <= {1} THEN '{{}}'::jsonb[] ELSE {0}[{1}+1:array_length({0}, 1)] END",
            PLAN_LOGS.RECENT_LOGS.dataType,
            PLAN_LOGS.RECENT_LOGS,
            DSL.inline(count)
        )
        return dsl.update(PLAN_LOGS)
            .set(PLAN_LOGS.RECENT_LOGS, trimmed)
            .where(PLAN_LOGS.PLAN_ID.eq(planId))
            .execute()
    }

    override fun updateCompaction(planId: UUID, compaction: String): Int {
        return dsl.update(PLAN_LOGS)
            .set(PLAN_LOGS.COMPACTION, compaction)
            .where(PLAN_LOGS.PLAN_ID.eq(planId))
            .execute()
    }
}
