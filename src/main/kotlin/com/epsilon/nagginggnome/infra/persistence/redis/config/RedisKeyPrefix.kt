package com.epsilon.nagginggnome.infra.persistence.redis.config

/**
 * Redis 키 Prefix 정의 enum
 */
enum class RedisKeyPrefix(
    private val value: String
) {

    /**
     * Refresh Token 키 Prefix
     */
    AUTH_REFRESH_TOKEN("auth:rt");

    /**
     * Redis 키 생성
     */
    fun key(vararg parts: Any): String =
        if (parts.isEmpty()) value else "$value:${parts.joinToString(":")}"
}
