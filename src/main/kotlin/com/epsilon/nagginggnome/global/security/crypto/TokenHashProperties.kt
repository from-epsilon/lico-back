package com.epsilon.nagginggnome.global.security.crypto

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "security.token-hash")
data class TokenHashProperties(
    val pepper: String
)
