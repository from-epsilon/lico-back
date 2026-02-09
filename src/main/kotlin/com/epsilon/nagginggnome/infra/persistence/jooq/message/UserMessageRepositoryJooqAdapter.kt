package com.epsilon.nagginggnome.infra.persistence.jooq.message

import com.epsilon.nagginggnome.domain.message.repository.UserMessageRepository
import com.epsilon.nagginggnome.domain.message.repository.model.UserMessageCreateModel
import com.epsilon.nagginggnome.generated.jooq.tables.references.USER_MESSAGES
import org.jooq.DSLContext
import org.springframework.stereotype.Repository
import java.time.Instant
import java.util.UUID

@Repository
class UserMessageRepositoryJooqAdapter(
    private val dsl: DSLContext
) : UserMessageRepository {

    override fun insert(model: UserMessageCreateModel) {
        dsl.insertInto(USER_MESSAGES)
            .set(USER_MESSAGES.ID, model.id)
            .set(USER_MESSAGES.USER_ID, model.userId)
            .set(USER_MESSAGES.PLAN_ID, model.planId)
            .set(USER_MESSAGES.BODY, model.body)
            .set(USER_MESSAGES.SERVER_MESSAGE_ID, model.serverMessageId)
            .set(USER_MESSAGES.SENT_AT, model.sentAt)
            .execute()
    }

    override fun existsById(id: UUID): Boolean {
        return dsl.fetchExists(
            dsl.selectOne()
                .from(USER_MESSAGES)
                .where(USER_MESSAGES.ID.eq(id))
        )
    }

    override fun existsByIdAndPlanId(id: UUID, planId: UUID): Boolean {
        return dsl.fetchExists(
            dsl.selectOne()
                .from(USER_MESSAGES)
                .where(USER_MESSAGES.ID.eq(id))
                .and(USER_MESSAGES.PLAN_ID.eq(planId))
        )
    }

    override fun updateContent(id: UUID, planId: UUID, body: String, sentAt: Instant): Int {
        return dsl.update(USER_MESSAGES)
            .set(USER_MESSAGES.BODY, body)
            .set(USER_MESSAGES.SENT_AT, sentAt)
            .where(USER_MESSAGES.ID.eq(id))
            .and(USER_MESSAGES.PLAN_ID.eq(planId))
            .execute()
    }
}
