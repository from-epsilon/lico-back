package com.epsilon.nagginggnome.domain.auth.dto.request

/**
 * 토큰 재발급 요청 DTO
 */
data class TokenReissueRequest(
    val refreshToken: String
)
