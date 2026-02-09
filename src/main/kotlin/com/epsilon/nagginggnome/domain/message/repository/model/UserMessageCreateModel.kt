package com.epsilon.nagginggnome.domain.message.repository.model

import java.time.Instant
import java.util.UUID

data class UserMessageCreateModel(
    val id: UUID,
    val userId: UUID,
    val planId: UUID,
    val body: String,
    val serverMessageId: Long,
    val sentAt: Instant
)
