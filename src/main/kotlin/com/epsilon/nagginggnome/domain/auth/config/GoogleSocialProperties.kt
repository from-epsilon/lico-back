package com.epsilon.nagginggnome.domain.auth.config

import org.springframework.boot.context.properties.ConfigurationProperties

/**
 * Google 소셜 로그인 설정값
 */
@ConfigurationProperties(prefix = "social.google")
data class GoogleSocialProperties(

    /**
     * Google OAuth Client ID
     * - ID Token의 aud 검증 기준 값
     */
    val clientId: String,

    /**
     * OIDC Discovery issuer
     * - Discovery 문서를 통해 jwks_uri 등을 자동 조회합니다.
     */
    val discoveryIssuer: String = "https://accounts.google.com",

    /**
     * iss 허용 목록입니다.
     */
    val allowedIssuers: List<String> = listOf(
        "accounts.google.com",
        "https://accounts.google.com"
    )
)
