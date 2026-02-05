package com.epsilon.nagginggnome.domain.message.repository

import com.epsilon.nagginggnome.domain.message.repository.model.ServerMessageCreateModel

interface ServerMessageRepository {
    fun insert(model: ServerMessageCreateModel): Long
}
