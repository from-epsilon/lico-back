package com.epsilon.nagginggnome.domain.push.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "fcm")
data class FcmProperties(
    val credentialsPath: String
)
