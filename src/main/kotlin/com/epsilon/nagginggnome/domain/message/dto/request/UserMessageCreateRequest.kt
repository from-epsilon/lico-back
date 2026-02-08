package com.epsilon.nagginggnome.domain.message.dto.request

import java.time.Instant
import java.util.UUID

/**
 * 사용자 메시지 전송 요청 DTO
 */
data class UserMessageCreateRequest(
    val id: UUID,
    val planId: UUID,
    val sentAt: Instant,
    val body: String,
    val serverMessageId: Long
)
