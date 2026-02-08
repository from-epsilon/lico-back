package com.epsilon.nagginggnome.domain.message.repository

import com.epsilon.nagginggnome.domain.message.repository.model.UserMessageCreateModel
import com.epsilon.nagginggnome.domain.message.repository.model.UserMessageQueryModel
import java.time.Instant
import java.util.UUID

interface UserMessageRepository {
    fun insert(model: UserMessageCreateModel): Long

    fun existsByClientMessageId(clientMessageId: UUID): Boolean

    fun findByClientMessageIdAndPlanId(clientMessageId: UUID, planId: UUID): UserMessageQueryModel?

    fun updateContent(clientMessageId: UUID, planId: UUID, body: String, sentAt: Instant?): Int
}
