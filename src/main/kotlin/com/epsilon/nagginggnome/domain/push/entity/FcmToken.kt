package com.epsilon.nagginggnome.domain.push.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Index
import jakarta.persistence.Table
import jakarta.persistence.UniqueConstraint
import org.hibernate.annotations.UuidGenerator
import java.time.Instant
import java.util.UUID

/**
 * 유저의 현재 유효한 FCM 토큰을 1개만 관리하는 엔티티
 *
 * 정책 요약
 * - 유저 1명당 1개 row만 유지합니다
 * - 마지막 로그인한 기기의 token으로 덮어쓰기
 */
@Entity
@Table(
    name = "fcm_tokens",
    uniqueConstraints = [
        UniqueConstraint(
            name = "uk_fcm_tokens_user_id",
            columnNames = ["user_id"]
        ),
        UniqueConstraint(
            name = "uk_fcm_tokens_token",
            columnNames = ["token"]
        )
    ],
    indexes = [
        Index(name = "idx_fcm_tokens_user_id", columnList = "user_id"),
        Index(name = "idx_fcm_tokens_token", columnList = "token")
    ]
)
class FcmToken(
    userId: UUID,
    token: String,
    lastLoginAt: Instant
) {

    /**
     * 고유 ID
     */
    @Id
    @UuidGenerator
    @Column(name = "id", nullable = false, updatable = false, columnDefinition = "uuid")
    var id: UUID? = null
        private set

    /**
     * 유저 ID
     */
    @Column(name = "user_id", nullable = false, columnDefinition = "uuid")
    var userId: UUID = userId
        private set

    /**
     * FCM 등록 토큰
     * - 푸시 수신 주소 역할
     */
    @Column(name = "token", nullable = false)
    var token: String = token
        private set

    /**
     * 마지막 로그인 시각
     */
    @Column(name = "last_login_at", nullable = false)
    var lastLoginAt: Instant = lastLoginAt
        private set

    /**
     * 마지막 로그인 기기 기준으로 토큰을 갱신
     */
    fun updateToLastLogin(newToken: String, newLastLoginAt: Instant) {
        this.token = newToken
        this.lastLoginAt = newLastLoginAt
    }
}
