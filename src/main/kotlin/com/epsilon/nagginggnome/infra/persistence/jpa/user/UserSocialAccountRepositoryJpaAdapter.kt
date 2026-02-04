package com.epsilon.nagginggnome.infra.persistence.jpa.user

import com.epsilon.nagginggnome.domain.user.constant.SocialProvider
import com.epsilon.nagginggnome.domain.user.entity.UserSocialAccount
import com.epsilon.nagginggnome.domain.user.repository.UserSocialAccountRepository
import org.springframework.stereotype.Repository

@Repository
class UserSocialAccountRepositoryJpaAdapter(
    private val jpa: JpaUserSocialAccountRepository
) : UserSocialAccountRepository {

    override fun findByProviderAndProviderUserId(
        provider: SocialProvider,
        providerUserId: String
    ): UserSocialAccount? {
        return jpa.findByProviderAndProviderUserId(provider, providerUserId)
    }

    override fun save(entity: UserSocialAccount): UserSocialAccount {
        return jpa.save(entity)
    }
}
