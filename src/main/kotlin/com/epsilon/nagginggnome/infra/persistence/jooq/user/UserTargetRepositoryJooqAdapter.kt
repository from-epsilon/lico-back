package com.epsilon.nagginggnome.infra.persistence.jooq.user

import com.epsilon.nagginggnome.domain.user.constant.UserStatus
import com.epsilon.nagginggnome.domain.user.repository.UserTargetRepository
import com.epsilon.nagginggnome.generated.jooq.tables.references.FCM_TOKENS
import com.epsilon.nagginggnome.generated.jooq.tables.references.USERS
import org.jooq.DSLContext
import org.springframework.stereotype.Repository
import java.time.Instant
import java.util.UUID

@Repository
class UserTargetRepositoryJooqAdapter(
    private val dsl: DSLContext
) : UserTargetRepository {

    override fun findAdditionalTargetUserIds(
        lastLoginCutoff: Instant,
        limit: Int
    ): List<UUID> {
        if (limit <= 0) {
            return emptyList()
        }

        return dsl
            .select(USERS.ID)
            .from(USERS)
            .join(FCM_TOKENS).on(FCM_TOKENS.USER_ID.eq(USERS.ID))
            .where(USERS.STATUS.eq(UserStatus.ACTIVE.name))
            .and(USERS.LAST_LOGIN_AT.ge(lastLoginCutoff))
            .orderBy(USERS.ID.asc())
            .limit(limit)
            .fetch(USERS.ID)
            .filterNotNull()
    }
}
