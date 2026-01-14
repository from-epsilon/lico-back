package com.epsilon.nagginggnome.domain.auth.repository

import java.time.Duration
import java.util.UUID

/**
 * Refresh Token 저장소 인터페이스
 */
interface RefreshTokenRepository {

    /**
     * Refresh Token 저장
     */
    fun save(
        userId: UUID,
        refreshTokenHash: String,
        ttl: Duration
    )

    /**
     * 저장된 Refresh Token 조회
     */
    fun find(
        userId: UUID
    ): String?

    /**
     * Refresh Token 상태 삭제
     */
    fun delete(
        userId: UUID
    ): Boolean
}
