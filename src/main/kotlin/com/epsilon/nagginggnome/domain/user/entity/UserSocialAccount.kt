package com.epsilon.nagginggnome.domain.user.entity

import com.epsilon.nagginggnome.domain.user.constant.SocialProvider
import com.epsilon.nagginggnome.global.entity.BaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.ForeignKey
import jakarta.persistence.Id
import jakarta.persistence.Index
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import jakarta.persistence.UniqueConstraint
import org.hibernate.annotations.UuidGenerator
import java.util.UUID

/**
 * 외부 소셜 계정과 User를 연결하는 엔티티
 *
 * 핵심 제약
 * - provider + providerUserId(sub)는 전역 유니크
 * - userId + provider도 유니크로 두어 동일 provider 중복 연결을 막음
 */
@Entity
@Table(
    name = "user_social_accounts",
    uniqueConstraints = [
        UniqueConstraint(
            name = "uk_social_provider_userid",
            columnNames = ["provider", "provider_user_id"]
        ),
        UniqueConstraint(
            name = "uk_social_user_provider",
            columnNames = ["user_id", "provider"]
        )
    ],
    indexes = [
        Index(name = "idx_social_user_id", columnList = "user_id")
    ]
)
class UserSocialAccount(
    user: User,
    provider: SocialProvider,
    providerUserId: String,
    emailAtProvider: String? = null
) : BaseEntity() {

    @Id
    @UuidGenerator
    @Column(name = "id", nullable = false, updatable = false, columnDefinition = "uuid")
    var id: UUID? = null
        private set

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
        name = "user_id",
        nullable = false,
        foreignKey = ForeignKey(name = "fk_user_social_accounts_user_id")
    )
    var user: User = user
        private set

    /**
     * 소셜 제공자(GOOGLE, APPLE)
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "provider", nullable = false)
    var provider: SocialProvider = provider
        private set

    /**
     * 제공자 내부 사용자 식별자(sub)
     */
    @Column(name = "provider_user_id", nullable = false)
    var providerUserId: String = providerUserId
        private set

    /**
     * provider에서 받은 이메일 원본
     */
    @Column(name = "email_at_provider")
    var emailAtProvider: String? = emailAtProvider
        private set
}
