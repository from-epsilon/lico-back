package com.epsilon.nagginggnome.domain.message.repository.model

import com.epsilon.nagginggnome.domain.message.constant.MessageType
import java.time.Instant
import java.util.UUID

data class ServerMessageQueryModel(
    val id: Long,
    val userId: UUID,
    val planId: UUID?,
    val type: MessageType,
    val title: String,
    val body: String,
    val dataJson: String?,
    val createdAt: Instant
)
