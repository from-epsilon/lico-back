package com.epsilon.nagginggnome.domain.auth.dto.request

import com.epsilon.nagginggnome.domain.user.constant.SocialProvider

/**
 * 소셜 로그인 요청 DTO
 */
data class SocialLoginRequest(

    /**
     * 소셜 제공자(GOOGLE, APPLE)
     */
    val provider: SocialProvider,

    /**
     * 제공자가 발급한 ID Token(JWT)
     */
    val idToken: String
)
