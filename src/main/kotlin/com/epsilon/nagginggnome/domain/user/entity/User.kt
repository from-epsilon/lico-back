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
class User : BaseEntity() {

    /**
     * 고유 ID
     */
    @Id
    @UuidGenerator
    @Column(name = "id", nullable = false, updatable = false, columnDefinition = "uuid")
    var id: UUID? = null
         private set

    /**
     * 별명
     */
    @Column(name = "nickname")
    var nickname: String? = null

    /**
     * 이메일
     */
    @Column(name = "email")
    var email: String? = null

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

    /**
     * 마지막 로그인 시각
     */
    @Column(name = "last_login_at")
    var lastLoginAt: Instant? = null
}
