package com.epsilon.nagginggnome.domain.message.repository

import com.epsilon.nagginggnome.domain.message.repository.model.ServerMessageCreateModel
import com.epsilon.nagginggnome.domain.message.repository.model.ServerMessageQueryModel
import java.util.UUID

interface ServerMessageRepository {
    fun insert(model: ServerMessageCreateModel): Long

    fun findByUserIdAndPlanId(userId: UUID, planId: UUID): List<ServerMessageQueryModel>

    fun findByUserId(userId: UUID): List<ServerMessageQueryModel>

    fun existsByIdAndUserIdAndPlanId(id: Long, userId: UUID, planId: UUID): Boolean
}
