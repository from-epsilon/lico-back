package com.epsilon.nagginggnome.domain.user.repository

import com.epsilon.nagginggnome.domain.user.constant.SocialProvider
import com.epsilon.nagginggnome.domain.user.entity.UserSocialAccount
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface UserSocialAccountRepository : JpaRepository<UserSocialAccount, UUID> {

    /**
     * 소셜 로그인 식별자(provider + sub)로 연결 정보를 조회
     *
     * - 존재하면 기존 유저 로그인 처리
     * - 없으면 신규 가입 처리
     */
    fun findByProviderAndProviderUserId(
        provider: SocialProvider,
        providerUserId: String
    ): UserSocialAccount?
}
