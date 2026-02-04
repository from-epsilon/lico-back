package com.epsilon.nagginggnome.infra.fcm.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "fcm")
data class FcmProperties(
    val credentialsPath: String
)
