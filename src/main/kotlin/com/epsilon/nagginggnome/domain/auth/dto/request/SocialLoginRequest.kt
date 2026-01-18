package com.epsilon.nagginggnome.domain.auth.dto.request

/**
 * 소셜 로그인 요청 DTO
 */
data class SocialLoginRequest(

    /**
     * 제공자가 발급한 ID Token(JWT)
     */
    val idToken: String
)
