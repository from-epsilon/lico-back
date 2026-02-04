package com.epsilon.nagginggnome.domain.user.repository

import com.epsilon.nagginggnome.domain.user.constant.SocialProvider
import com.epsilon.nagginggnome.domain.user.entity.UserSocialAccount

interface UserSocialAccountRepository {
    fun findByProviderAndProviderUserId(provider: SocialProvider, providerUserId: String): UserSocialAccount?
    fun save(entity: UserSocialAccount): UserSocialAccount
}
