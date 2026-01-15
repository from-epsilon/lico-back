package com.epsilon.nagginggnome.domain.auth.repository.impl

import com.epsilon.nagginggnome.domain.auth.repository.RefreshTokenRepository
import com.epsilon.nagginggnome.global.redis.RedisKeyPrefix
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.stereotype.Repository
import java.time.Duration
import java.time.Instant
import java.util.*

/**
 * Refresh Token Repository Redis 구현체
 */
@Repository
class RedisRefreshTokenRepository(
    private val redis: StringRedisTemplate
) : RefreshTokenRepository {

    override fun save(userId: UUID, refreshTokenHash: String, expiresAt: Instant) {
        val key = RedisKeyPrefix.AUTH_REFRESH_TOKEN.key(userId)
        val ttl = Duration.between(Instant.now(), expiresAt).coerceAtLeast(Duration.ZERO)
        redis.opsForValue().set(key, refreshTokenHash, ttl)
    }

    override fun find(userId: UUID): String? {
        val key = RedisKeyPrefix.AUTH_REFRESH_TOKEN.key(userId)
        return redis.opsForValue().get(key)
    }

    override fun delete(userId: UUID): Boolean {
        val key = RedisKeyPrefix.AUTH_REFRESH_TOKEN.key(userId)
        return redis.delete(key)
    }
}
