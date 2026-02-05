package com.epsilon.nagginggnome.domain.user.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "user")
data class UserProperties(
    val dormancyLastLoginDays: Long
)
