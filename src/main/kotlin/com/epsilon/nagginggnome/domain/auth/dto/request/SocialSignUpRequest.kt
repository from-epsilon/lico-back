package com.epsilon.nagginggnome.domain.auth.dto.request

/**
 * 소셜 회원가입 요청 DTO
 */
data class SocialSignUpRequest(

    /**
     * 제공자가 발급한 ID Token(JWT)
     */
    val idToken: String
)
