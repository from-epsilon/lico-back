package com.epsilon.nagginggnome.infra.persistence.jooq.message

import com.epsilon.nagginggnome.domain.message.constant.MessageType
import com.epsilon.nagginggnome.domain.message.repository.ServerMessageRepository
import com.epsilon.nagginggnome.domain.message.repository.model.ServerMessageCreateModel
import com.epsilon.nagginggnome.domain.message.repository.model.ServerMessageQueryModel
import com.epsilon.nagginggnome.generated.jooq.tables.references.SERVER_MESSAGES
import org.jooq.DSLContext
import org.jooq.JSONB
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
class ServerMessageRepositoryJooqAdapter(
    private val dsl: DSLContext
) : ServerMessageRepository {

    override fun insert(model: ServerMessageCreateModel): Long {
        return dsl.insertInto(SERVER_MESSAGES)
            .set(SERVER_MESSAGES.USER_ID, model.userId)
            .set(SERVER_MESSAGES.PLAN_ID, model.planId)
            .set(SERVER_MESSAGES.TITLE, model.title)
            .set(SERVER_MESSAGES.BODY, model.body)
            .set(SERVER_MESSAGES.DATA_JSON, model.dataJson?.let(JSONB::valueOf))
            .set(SERVER_MESSAGES.TYPE, model.type.name)
            .set(SERVER_MESSAGES.LLM_META_JSON, model.llmMetaJson?.let(JSONB::valueOf))
            .returning(SERVER_MESSAGES.ID)
            .fetchOne()
            ?.id
            ?: 0L
    }

    override fun findByUserIdAndPlanId(userId: UUID, planId: UUID): List<ServerMessageQueryModel> {
        return dsl.select(
            SERVER_MESSAGES.ID,
            SERVER_MESSAGES.USER_ID,
            SERVER_MESSAGES.PLAN_ID,
            SERVER_MESSAGES.TYPE,
            SERVER_MESSAGES.TITLE,
            SERVER_MESSAGES.BODY,
            SERVER_MESSAGES.DATA_JSON,
            SERVER_MESSAGES.CREATED_AT
        )
            .from(SERVER_MESSAGES)
            .where(SERVER_MESSAGES.USER_ID.eq(userId))
            .and(SERVER_MESSAGES.PLAN_ID.eq(planId))
            .orderBy(SERVER_MESSAGES.CREATED_AT.desc())
            .fetch { rec ->
                val type = MessageType.from(rec.get(SERVER_MESSAGES.TYPE)) ?: MessageType.ADDITIONAL
                ServerMessageQueryModel(
                    id = rec.get(SERVER_MESSAGES.ID) ?: 0L,
                    userId = rec.get(SERVER_MESSAGES.USER_ID)!!,
                    planId = rec.get(SERVER_MESSAGES.PLAN_ID),
                    type = type,
                    title = rec.get(SERVER_MESSAGES.TITLE) ?: "",
                    body = rec.get(SERVER_MESSAGES.BODY) ?: "",
                    dataJson = rec.get(SERVER_MESSAGES.DATA_JSON)?.data(),
                    createdAt = rec.get(SERVER_MESSAGES.CREATED_AT)!!
                )
            }
    }

    override fun findByUserId(userId: UUID): List<ServerMessageQueryModel> {
        return dsl.select(
            SERVER_MESSAGES.ID,
            SERVER_MESSAGES.USER_ID,
            SERVER_MESSAGES.PLAN_ID,
            SERVER_MESSAGES.TYPE,
            SERVER_MESSAGES.TITLE,
            SERVER_MESSAGES.BODY,
            SERVER_MESSAGES.DATA_JSON,
            SERVER_MESSAGES.CREATED_AT
        )
            .from(SERVER_MESSAGES)
            .where(SERVER_MESSAGES.USER_ID.eq(userId))
            .orderBy(SERVER_MESSAGES.CREATED_AT.desc())
            .fetch { rec ->
                val type = MessageType.from(rec.get(SERVER_MESSAGES.TYPE)) ?: MessageType.ADDITIONAL
                ServerMessageQueryModel(
                    id = rec.get(SERVER_MESSAGES.ID) ?: 0L,
                    userId = rec.get(SERVER_MESSAGES.USER_ID)!!,
                    planId = rec.get(SERVER_MESSAGES.PLAN_ID),
                    type = type,
                    title = rec.get(SERVER_MESSAGES.TITLE) ?: "",
                    body = rec.get(SERVER_MESSAGES.BODY) ?: "",
                    dataJson = rec.get(SERVER_MESSAGES.DATA_JSON)?.data(),
                    createdAt = rec.get(SERVER_MESSAGES.CREATED_AT)!!
                )
            }
    }

    override fun existsByIdAndUserIdAndPlanId(id: Long, userId: UUID, planId: UUID): Boolean {
        return dsl.fetchExists(
            dsl.selectOne()
                .from(SERVER_MESSAGES)
                .where(SERVER_MESSAGES.ID.eq(id))
                .and(SERVER_MESSAGES.USER_ID.eq(userId))
                .and(
                    SERVER_MESSAGES.PLAN_ID.eq(planId)
                        .or(SERVER_MESSAGES.PLAN_ID.isNull)
                )
        )
    }
}
