package com.epsilon.nagginggnome.domain.user.entity

import com.epsilon.nagginggnome.domain.user.constant.Role
import com.epsilon.nagginggnome.domain.user.constant.UserStatus
import com.epsilon.nagginggnome.global.entity.BaseEntity
import jakarta.persistence.*
import org.hibernate.annotations.UuidGenerator
import java.time.Instant
import java.util.*

@Entity
@Table(name = "users")
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
     * UserDetail은 나중에 생성될 수 있으므로 nullable
     * - mappedBy: 연관관계 주인이 UserDetail.user임을 의미
     */
    @OneToOne(mappedBy = "user", fetch = FetchType.LAZY, optional = true)
    var userDetail: UserDetail? = null
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

    /**
     * 연관관계 편의 메서드
     * - 양방향 동기화 목적임
     * - UserDetail 저장은 owning side(UserDetail.user)에서 발생하므로,
     *   반드시 detail.attachUser(this)도 같이 호출되어야 함
     */
    fun attachDetail(userDetail: UserDetail) {
        this.userDetail = userDetail
        userDetail.attachUser(this)
    }
}
