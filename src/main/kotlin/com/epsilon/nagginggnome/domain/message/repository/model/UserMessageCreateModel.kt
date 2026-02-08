package com.epsilon.nagginggnome.domain.message.repository.model

import com.epsilon.nagginggnome.domain.message.constant.UserMessageType
import java.time.Instant
import java.util.UUID

data class UserMessageCreateModel(
    val planId: UUID,
    val snapshotId: UUID,
    val snapshotVersion: Int,
    val body: String,
    val type: UserMessageType,
    val clientMessageId: UUID,
    val serverMessageId: Long,
    val sentAt: Instant
)
