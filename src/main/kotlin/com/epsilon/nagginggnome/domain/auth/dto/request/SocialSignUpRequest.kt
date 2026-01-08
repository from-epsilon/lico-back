package com.epsilon.nagginggnome.domain.auth.dto.request

import com.epsilon.nagginggnome.domain.user.constant.SocialProvider

/**
 * 소셜 회원가입 요청 DTO
 */
data class SocialSignUpRequest(

    /**
     * 소셜 제공자(GOOGLE, APPLE)
     */
    val provider: SocialProvider,

    /**
     * 제공자가 발급한 ID Token(JWT)
     */
    val idToken: String
)
