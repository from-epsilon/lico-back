package com.epsilon.nagginggnome.domain.message.repository.model

import com.epsilon.nagginggnome.domain.message.constant.MessageType
import java.util.UUID

data class ServerMessageCreateModel(
    val userId: UUID,
    val planId: UUID,
    val type: MessageType,
    val title: String,
    val body: String,
    val dataJson: String,
    val llmMetaJson: String
)
