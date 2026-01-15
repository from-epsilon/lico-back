package com.epsilon.nagginggnome.domain.auth.dto.response

/**
 * 토큰 재발급 응답 DTO
 */
data class TokenReissueResponse(

    /**
     * Access Token(JWT)
     */
    val accessToken: String
)
