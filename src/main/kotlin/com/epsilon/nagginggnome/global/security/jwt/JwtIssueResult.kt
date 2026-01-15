package com.epsilon.nagginggnome.global.security.jwt

import java.time.Instant

data class JwtIssueResult(
    val token: String,
    val expiresAt: Instant
)
