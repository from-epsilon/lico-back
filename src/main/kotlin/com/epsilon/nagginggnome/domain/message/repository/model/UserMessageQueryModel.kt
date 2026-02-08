package com.epsilon.nagginggnome.domain.message.repository.model

import com.epsilon.nagginggnome.domain.message.constant.UserMessageType
import java.util.UUID

data class UserMessageQueryModel(
    val id: Long,
    val planId: UUID,
    val type: UserMessageType,
    val clientMessageId: UUID,
    val serverMessageId: Long,
)
