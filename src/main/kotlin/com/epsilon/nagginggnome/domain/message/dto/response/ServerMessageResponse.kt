package com.epsilon.nagginggnome.domain.message.dto.response

import com.epsilon.nagginggnome.domain.message.constant.MessageType
import java.time.Instant
import java.util.UUID

/**
 * 서버 메시지 조회 응답 DTO
 */
data class ServerMessageResponse(
    val id: Long,
    val planId: UUID,
    val type: MessageType,
    val createdAt: Instant,
    val replyToId: UUID?,
    val data: Payload
) {
    data class Payload(
        val title: String?,
        val body: String,
        val dataJson: Map<String, Any?>?
    )
}
