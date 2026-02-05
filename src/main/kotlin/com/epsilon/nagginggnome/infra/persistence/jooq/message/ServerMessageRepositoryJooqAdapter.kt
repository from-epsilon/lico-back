package com.epsilon.nagginggnome.infra.persistence.jooq.message

import com.epsilon.nagginggnome.domain.message.repository.ServerMessageRepository
import com.epsilon.nagginggnome.domain.message.repository.model.ServerMessageCreateModel
import com.epsilon.nagginggnome.generated.jooq.tables.references.SERVER_MESSAGES
import org.jooq.DSLContext
import org.jooq.JSONB
import org.springframework.stereotype.Repository

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
}
