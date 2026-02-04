package com.epsilon.nagginggnome.domain.auth.service

import com.epsilon.nagginggnome.domain.auth.dto.request.SocialLoginRequest
import com.epsilon.nagginggnome.domain.auth.dto.response.SocialLoginResponse
import com.epsilon.nagginggnome.domain.auth.repository.RefreshTokenRepository
import com.epsilon.nagginggnome.infra.social.google.validator.GoogleIdTokenVerifier
import com.epsilon.nagginggnome.domain.user.constant.SocialProvider
import com.epsilon.nagginggnome.domain.user.entity.User
import com.epsilon.nagginggnome.domain.user.entity.UserSocialAccount
import com.epsilon.nagginggnome.domain.user.repository.UserRepository
import com.epsilon.nagginggnome.domain.user.repository.UserSocialAccountRepository
import com.epsilon.nagginggnome.global.constant.code.CommonErrorCode
import com.epsilon.nagginggnome.global.exception.ApiException
import com.epsilon.nagginggnome.global.security.crypto.TokenHasher
import com.epsilon.nagginggnome.global.security.jwt.JwtConstants
import com.epsilon.nagginggnome.global.security.jwt.JwtProvider
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Instant

@Service
class GoogleAuthService(
    private val userRepository: UserRepository,
    private val userSocialAccountRepository: UserSocialAccountRepository,
    private val refreshTokenRepository: RefreshTokenRepository,
    private val googleIdTokenVerifier: GoogleIdTokenVerifier,
    private val jwtProvider: JwtProvider,
    private val tokenHasher: TokenHasher
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
            ?.let { login(it.user, emailAtProvider, Instant.now()) }
            ?: signUp(providerUserId, emailAtProvider, Instant.now())
    }

    /**
     * 기존 사용자 로그인 처리 메서드
     */
    private fun login(user: User, emailAtProvider: String?, now: Instant): SocialLoginResponse {
        // 로그인 시각 갱신
        user.updateToLastLogin(now)

        // 사용자 이메일이 비어있고 provider 이메일이 있으면 보정
        if (user.email.isNullOrBlank()) {
            emailAtProvider
                ?.takeIf { it.isNotBlank() }
                ?.let(user::changeEmail)
        }

        // 토큰 발급 및 응답 생성
        return issueTokens(user, isNewUser = false, now)
    }

    /**
     * 신규 사용자 자동 회원가입 처리 메서드
     */
    private fun signUp(providerUserId: String, emailAtProvider: String?, now: Instant): SocialLoginResponse =
        try {
            // 신규 유저 생성 및 저장
            val newUser = userRepository.save(
                User(
                    email = emailAtProvider,
                    lastLoginAt = now
                )
            )

            // 소셜 계정 연결 엔티티 생성 및 저장
            userSocialAccountRepository.save(
                UserSocialAccount(
                    user = newUser,
                    provider = SocialProvider.GOOGLE,
                    providerUserId = providerUserId,
                    emailAtProvider = emailAtProvider
                )
            )

            // 토큰 발급 및 응답 생성
            issueTokens(newUser, isNewUser = true, now)

        } catch (_: DataIntegrityViolationException) {
            val existingAccount = userSocialAccountRepository.findByProviderAndProviderUserId(
                SocialProvider.GOOGLE,
                providerUserId
            ) ?: throw ApiException(CommonErrorCode.CONFLICT)

            login(existingAccount.user, emailAtProvider, now)
        }


    private fun issueTokens(user: User, isNewUser: Boolean, now: Instant): SocialLoginResponse {
        val userId = user.id ?: throw ApiException(CommonErrorCode.INTERNAL_SERVER_ERROR)

        // AccessToken, RefreshToken 발급(issuedAt/exp 일관성을 위해 now 공유)
        val issuedAccess = jwtProvider.generateAccessToken(userId, user.role, now)
        val issuedRefresh = jwtProvider.generateRefreshToken(userId, now)

        // Refresh Token은 원문 저장이 아니라 해시만 저장
        val refreshTokenHash = tokenHasher.hash(issuedRefresh.token)

        refreshTokenRepository.save(
            userId = userId,
            refreshTokenHash = refreshTokenHash,
            expiresAt = issuedRefresh.expiresAt
        )

        return SocialLoginResponse(
            accessToken = issuedAccess.token,
            refreshToken = issuedRefresh.token,
            isNewUser = isNewUser
        )
    }
}


