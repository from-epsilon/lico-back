package com.epsilon.nagginggnome.domain.message.dto.response

import com.epsilon.nagginggnome.domain.message.constant.ChatMessageType
import java.time.Instant

data class ChatMessageListItemResponse(
    val messageId: Long,
    val snapshotId: Long,
    val snapshotVersion: Int,
    val type: ChatMessageType,
    val content: String,
    val createdAt: Instant
)
