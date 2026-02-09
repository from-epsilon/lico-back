package com.epsilon.nagginggnome.domain.message.repository

import com.epsilon.nagginggnome.domain.message.repository.model.UserMessageCreateModel
import java.time.Instant
import java.util.UUID

interface UserMessageRepository {
    fun insert(model: UserMessageCreateModel)

    fun existsById(id: UUID): Boolean

    fun existsByIdAndPlanId(id: UUID, planId: UUID): Boolean

    fun updateContent(id: UUID, planId: UUID, body: String, sentAt: Instant): Int
}
