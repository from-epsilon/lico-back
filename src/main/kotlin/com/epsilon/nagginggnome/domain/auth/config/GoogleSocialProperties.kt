package com.epsilon.nagginggnome.domain.auth.config

import org.springframework.boot.context.properties.ConfigurationProperties

/**
 * Google 소셜 로그인 설정값
 */
@ConfigurationProperties(prefix = "social.google")
data class GoogleSocialProperties(
    val clientId: String
)
