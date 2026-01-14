package com.epsilon.nagginggnome.global.redis

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
    fun key(id: String): String {
        // prefix와 id를 ':'로 결합하여 일관된 스캔 패턴을 유지
        return "${value}:${id}"
    }
}
