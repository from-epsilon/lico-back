package com.epsilon.nagginggnome.domain.message.dto.response

import com.epsilon.nagginggnome.domain.message.constant.ChatMessageType
import java.time.Instant
import java.util.UUID

/**
 * 채팅 메시지 리스트 조회 응답 DTO
 */
data class ChatMessageListItemResponse(
    val messageId: Long,
    val snapshotId: UUID,
    val snapshotVersion: Int,
    val type: ChatMessageType,
    val content: String,
    val createdAt: Instant
)
