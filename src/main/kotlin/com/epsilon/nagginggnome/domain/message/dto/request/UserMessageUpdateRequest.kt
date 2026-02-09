package com.epsilon.nagginggnome.domain.message.dto.request

import java.time.Instant

/**
 * 사용자 메시지 수정 요청 DTO
 */
data class UserMessageUpdateRequest(
    val body: String,
    val sentAt: Instant?
)
