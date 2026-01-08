package com.epsilon.nagginggnome.domain.auth.dto.response

/**
 * 소셜 로그인 응답 DTO
 */
data class SocialLoginResponse(

    /**
     * Access Token(JWT)
     */
    val accessToken: String,

    /**
     * Refresh Token(JWT)
     */
    val refreshToken: String
)
