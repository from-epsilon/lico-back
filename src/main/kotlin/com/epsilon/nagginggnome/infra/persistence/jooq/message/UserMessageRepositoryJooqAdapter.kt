package com.epsilon.nagginggnome.infra.persistence.jooq.message

import com.epsilon.nagginggnome.domain.message.constant.UserMessageType
import com.epsilon.nagginggnome.domain.message.repository.UserMessageRepository
import com.epsilon.nagginggnome.domain.message.repository.model.UserMessageCreateModel
import com.epsilon.nagginggnome.domain.message.repository.model.UserMessageQueryModel
import com.epsilon.nagginggnome.generated.jooq.tables.references.USER_MESSAGES
import org.jooq.DSLContext
import org.springframework.stereotype.Repository
import java.time.Instant
import java.util.UUID

@Repository
class UserMessageRepositoryJooqAdapter(
    private val dsl: DSLContext
) : UserMessageRepository {

    override fun insert(model: UserMessageCreateModel): Long {
        return dsl.insertInto(USER_MESSAGES)
            .set(USER_MESSAGES.PLAN_ID, model.planId)
            .set(USER_MESSAGES.SNAPSHOT_ID, model.snapshotId)
            .set(USER_MESSAGES.SNAPSHOT_VERSION, model.snapshotVersion)
            .set(USER_MESSAGES.CONTENT, model.body)
            .set(USER_MESSAGES.TYPE, model.type.name)
            .set(USER_MESSAGES.CLIENT_MESSAGE_ID, model.clientMessageId)
            .set(USER_MESSAGES.SERVER_MESSAGE_ID, model.serverMessageId)
            .set(USER_MESSAGES.SENT_AT, model.sentAt)
            .returning(USER_MESSAGES.ID)
            .fetchOne()
            ?.id
            ?: 0L
    }

    override fun existsByClientMessageId(clientMessageId: UUID): Boolean {
        return dsl.fetchExists(
            dsl.selectOne()
                .from(USER_MESSAGES)
                .where(USER_MESSAGES.CLIENT_MESSAGE_ID.eq(clientMessageId))
        )
    }

    override fun findByClientMessageIdAndPlanId(clientMessageId: UUID, planId: UUID): UserMessageQueryModel? {
        return dsl.select(
            USER_MESSAGES.ID,
            USER_MESSAGES.PLAN_ID,
            USER_MESSAGES.TYPE,
            USER_MESSAGES.CLIENT_MESSAGE_ID,
            USER_MESSAGES.SERVER_MESSAGE_ID
        )
            .from(USER_MESSAGES)
            .where(USER_MESSAGES.CLIENT_MESSAGE_ID.eq(clientMessageId))
            .and(USER_MESSAGES.PLAN_ID.eq(planId))
            .fetchOne { rec ->
                val type = UserMessageType.valueOf(rec.get(USER_MESSAGES.TYPE) ?: UserMessageType.USER_REPLY.name)
                UserMessageQueryModel(
                    id = rec.get(USER_MESSAGES.ID) ?: 0L,
                    planId = rec.get(USER_MESSAGES.PLAN_ID)!!,
                    type = type,
                    clientMessageId = rec.get(USER_MESSAGES.CLIENT_MESSAGE_ID)!!,
                    serverMessageId = rec.get(USER_MESSAGES.SERVER_MESSAGE_ID) ?: 0L,
                )
            }
    }

    override fun updateContent(clientMessageId: UUID, planId: UUID, body: String, sentAt: Instant?): Int {
        return dsl.update(USER_MESSAGES)
            .set(USER_MESSAGES.CONTENT, body)
            .set(USER_MESSAGES.SENT_AT, sentAt)
            .set(USER_MESSAGES.UPDATED_AT, Instant.now())
            .where(USER_MESSAGES.CLIENT_MESSAGE_ID.eq(clientMessageId))
            .and(USER_MESSAGES.PLAN_ID.eq(planId))
            .execute()
    }
}
