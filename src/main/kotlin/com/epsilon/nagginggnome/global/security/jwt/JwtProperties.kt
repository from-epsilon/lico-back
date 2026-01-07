package com.epsilon.nagginggnome.global.security.jwt

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "jwt")
data class JwtProperties(
    val secret: String,
    val accessTokenExp: Long,
    val refreshTokenExp: Long,
    val issuer: String
)
