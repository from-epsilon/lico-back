package com.epsilon.nagginggnome.infra.persistence.jooq.user

import com.epsilon.nagginggnome.domain.user.constant.UserStatus
import com.epsilon.nagginggnome.domain.user.repository.PushBatchTargetRepository
import com.epsilon.nagginggnome.domain.user.repository.model.PushBatchTarget
import com.epsilon.nagginggnome.generated.jooq.tables.references.FCM_TOKENS
import com.epsilon.nagginggnome.generated.jooq.tables.references.USERS
import com.epsilon.nagginggnome.generated.jooq.tables.references.USER_SETTINGS
import org.jooq.DSLContext
import org.springframework.stereotype.Repository
import java.time.Instant

@Repository
class PushBatchTargetRepositoryJooqAdapter(
    private val dsl: DSLContext
) : PushBatchTargetRepository {

    override fun findTargetsForPushBatch(
        lastLoginCutoff: Instant,
        limit: Int
    ): List<PushBatchTarget> {
        if (limit <= 0) {
            return emptyList()
        }

        return dsl
            .select(USERS.ID, USER_SETTINGS.TIMEZONE)
            .from(USERS)
            .join(FCM_TOKENS).on(FCM_TOKENS.USER_ID.eq(USERS.ID))
            .join(USER_SETTINGS).on(USER_SETTINGS.USER_ID.eq(USERS.ID))
            .where(
                USERS.STATUS.eq(UserStatus.ACTIVE.name)
                    .and(USERS.LAST_LOGIN_AT.ge(lastLoginCutoff))
            )
            .orderBy(USERS.ID.asc())
            .limit(limit)
            .fetch { record ->
                val userId = record.get(USERS.ID) ?: return@fetch null
                val timezone = record.get(USER_SETTINGS.TIMEZONE) ?: return@fetch null
                PushBatchTarget(userId = userId, timezone = timezone)
            }
            .filterNotNull()
    }
}
