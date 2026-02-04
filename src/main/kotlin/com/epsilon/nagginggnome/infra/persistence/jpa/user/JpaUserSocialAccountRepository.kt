package com.epsilon.nagginggnome.infra.persistence.jpa.user

import com.epsilon.nagginggnome.domain.user.constant.SocialProvider
import com.epsilon.nagginggnome.domain.user.entity.UserSocialAccount
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface JpaUserSocialAccountRepository : JpaRepository<UserSocialAccount, UUID> {
    fun findByProviderAndProviderUserId(provider: SocialProvider, providerUserId: String): UserSocialAccount?
}
