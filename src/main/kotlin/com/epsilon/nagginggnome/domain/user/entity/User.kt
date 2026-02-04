package com.epsilon.nagginggnome.domain.user.entity

import com.epsilon.nagginggnome.domain.user.constant.Role
import com.epsilon.nagginggnome.domain.user.constant.UserStatus
import com.epsilon.nagginggnome.global.entity.BaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.hibernate.annotations.UuidGenerator
import java.time.Instant
import java.util.UUID

@Entity
@Table(
    name = "users"
)
class User(
    email: String?,
    lastLoginAt: Instant
) : BaseEntity() {

    /**
     * 고유 ID
     */
    @Id
    @UuidGenerator
    @Column(name = "id", nullable = false, updatable = false, columnDefinition = "uuid")
    var id: UUID? = null
        private set

    /**
     * 이메일
     */
    @Column(name = "email")
    var email: String? = email
        private set

    /**
     * 역할
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    var role: Role = Role.USER
        private set

    /**
     * 상태
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    var status: UserStatus = UserStatus.ACTIVE
        private set

    /**
     * 마지막 로그인 시각
     */
    @Column(name = "last_login_at", nullable = false)
    var lastLoginAt: Instant = lastLoginAt
        private set

    /**
     * 마지막 로그인 기준으로 갱신
     */
    fun updateToLastLogin(newLastLoginAt: Instant) {
        this.lastLoginAt = newLastLoginAt
    }

    /**
     * 이메일 변경
     */
    fun changeEmail(email: String) {
        this.email = email
    }
}
