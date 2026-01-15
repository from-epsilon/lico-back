package com.epsilon.nagginggnome.domain.auth.dto.request

/**
 * 로그아웃 요청 DTO
 */
data class LogoutRequest(
    val refreshToken: String
)
