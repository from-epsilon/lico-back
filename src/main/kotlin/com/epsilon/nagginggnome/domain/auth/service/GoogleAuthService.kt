package com.epsilon.nagginggnome.domain.auth.service

import com.epsilon.nagginggnome.domain.auth.dto.request.SocialLoginRequest
import com.epsilon.nagginggnome.domain.auth.dto.response.SocialLoginResponse
import com.epsilon.nagginggnome.domain.auth.validator.GoogleIdTokenVerifier
import com.epsilon.nagginggnome.domain.user.constant.SocialProvider
import com.epsilon.nagginggnome.domain.user.entity.User
import com.epsilon.nagginggnome.domain.user.entity.UserSocialAccount
import com.epsilon.nagginggnome.domain.user.repository.UserRepository
import com.epsilon.nagginggnome.domain.user.repository.UserSocialAccountRepository
import com.epsilon.nagginggnome.global.constant.code.CommonErrorCode
import com.epsilon.nagginggnome.global.exception.ApiException
import com.epsilon.nagginggnome.global.security.jwt.JwtConstants
import com.epsilon.nagginggnome.global.security.jwt.JwtTokenProvider
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Instant

@Service
class GoogleAuthService(
    private val userRepository: UserRepository,
    private val userSocialAccountRepository: UserSocialAccountRepository,
    private val googleIdTokenVerifier: GoogleIdTokenVerifier,
    private val jwtTokenProvider: JwtTokenProvider
) {

    /**
     * Google 로그인 또는 자동 회원가입 처리 메서드
     *
     * 처리 흐름
     * - idToken 검증 및 Jwt 클레임 추출(sub, email)
     * - (provider, sub)로 UserSocialAccount 조회
     * - 있으면 로그인 처리, 없으면 자동 가입 처리
     */
    @Transactional
    fun loginOrSignUp(req: SocialLoginRequest): SocialLoginResponse {
        // Google ID Token 검증 및 디코딩 수행
        val token = googleIdTokenVerifier.verifyAndDecode(req.idToken)

        // Google 고유 식별자(sub) 추출
        val providerUserId = token.subject
            ?.takeIf { it.isNotBlank() }
            ?: throw ApiException(CommonErrorCode.UNAUTHORIZED)

        // Google이 제공한 이메일 클레임 추출
        val emailAtProvider = token.getClaimAsString(JwtConstants.CLAIM_EMAIL)

        // 기존 소셜 계정 존재 시 로그인 처리, 없으면 가입 처리
        return userSocialAccountRepository
            .findByProviderAndProviderUserId(SocialProvider.GOOGLE, providerUserId)
            ?.let { login(it.user, emailAtProvider) }
            ?: signUp(providerUserId, emailAtProvider)
    }

    /**
     * 기존 사용자 로그인 처리 메서드
     */
    private fun login(user: User, emailAtProvider: String?): SocialLoginResponse {
        // 로그인 시각 갱신
        user.lastLoginAt = Instant.now()

        // 사용자 이메일이 비어있고 provider 이메일이 있으면 보정
        if (user.email.isNullOrBlank()) {
            user.email = emailAtProvider?.takeIf { it.isNotBlank() }
        }

        // 토큰 발급 및 응답 생성
        return issueTokens(user, isNewUser = false)
    }

    /**
     * 신규 사용자 자동 회원가입 처리 메서드
     */
    private fun signUp(providerUserId: String, emailAtProvider: String?): SocialLoginResponse =
        try {
            // 신규 유저 생성 및 저장
            val newUser = User(
                id = null,
                nickname = null,
                email = emailAtProvider,
                lastLoginAt = Instant.now()
            )
            val savedUser = userRepository.save(newUser)

            // 소셜 계정 연결 엔티티 생성 및 저장
            val socialAccount = UserSocialAccount().apply {
                user = savedUser
                provider = SocialProvider.GOOGLE
                this.providerUserId = providerUserId
                this.emailAtProvider = emailAtProvider
            }
            userSocialAccountRepository.save(socialAccount)

            // 토큰 발급 및 응답 생성
            issueTokens(savedUser, isNewUser = true)

        } catch (e: DataIntegrityViolationException) {
            val existingAccount = userSocialAccountRepository.findByProviderAndProviderUserId(
                SocialProvider.GOOGLE,
                providerUserId
            ) ?: throw ApiException(CommonErrorCode.CONFLICT)

            login(existingAccount.user, emailAtProvider)
        }


    private fun issueTokens(user: User, isNewUser: Boolean): SocialLoginResponse {
        val userId = user.id ?: throw ApiException(CommonErrorCode.INTERNAL_SERVER_ERROR)

        // AccessToken, RefreshToken 발급
        val accessToken = jwtTokenProvider.generateAccessToken(userId, user.role)
        val refreshToken = jwtTokenProvider.generateRefreshToken(userId)

        return SocialLoginResponse(
            accessToken = accessToken,
            refreshToken = refreshToken,
            isNewUser = isNewUser
        )
    }
}
