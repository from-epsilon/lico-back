package com.epsilon.nagginggnome.infra.persistence.jooq.user

import com.epsilon.nagginggnome.domain.user.constant.UserStatus
import com.epsilon.nagginggnome.domain.user.repository.UserSummaryRepository
import com.epsilon.nagginggnome.generated.jooq.tables.references.USERS
import com.epsilon.nagginggnome.generated.jooq.tables.references.USER_SUMMARIES
import org.jooq.DSLContext
import org.jooq.JSONB
import org.jooq.impl.DSL
import org.springframework.stereotype.Repository
import java.time.Instant
import java.util.UUID

@Repository
class UserSummaryRepositoryJooqAdapter(
    private val dsl: DSLContext
) : UserSummaryRepository {

    override fun findUserIdsDueForSummary(
        summaryCutoff: Instant,
        lastLoginCutoff: Instant,
        limit: Int
    ): List<UUID> {
        if (limit <= 0) {
            return emptyList()
        }

        val lastSummaries = DSL
            .select(
                USER_SUMMARIES.USER_ID,
                DSL.max(USER_SUMMARIES.CREATED_AT).`as`("last_created_at")
            )
            .from(USER_SUMMARIES)
            .groupBy(USER_SUMMARIES.USER_ID)
            .asTable("last_user_summaries")

        val lastUserId = requireNotNull(lastSummaries.field(USER_SUMMARIES.USER_ID))
        val lastCreatedAt = requireNotNull(
            lastSummaries.field("last_created_at", Instant::class.java)
        )

        return dsl
            .select(USERS.ID)
            .from(USERS)
            .leftJoin(lastSummaries).on(lastUserId.eq(USERS.ID))
            .where(
                lastCreatedAt.isNull
                    .or(lastCreatedAt.le(summaryCutoff))
            )
            .and(USERS.STATUS.eq(UserStatus.ACTIVE.name))
            .and(USERS.LAST_LOGIN_AT.ge(lastLoginCutoff))
            .orderBy(USERS.ID.asc())
            .limit(limit)
            .fetch(USERS.ID)
            .filterNotNull()
    }

    override fun insertSummary(userId: UUID, summaryJson: String): Int {
        return dsl.insertInto(USER_SUMMARIES)
            .set(USER_SUMMARIES.USER_ID, userId)
            .set(USER_SUMMARIES.SUMMARY_JSON, JSONB.valueOf(summaryJson))
            .execute()
    }
}
