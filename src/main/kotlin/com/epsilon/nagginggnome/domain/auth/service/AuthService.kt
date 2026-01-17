package com.epsilon.nagginggnome.domain.auth.service

import com.epsilon.nagginggnome.domain.auth.dto.request.LogoutRequest
import com.epsilon.nagginggnome.domain.auth.dto.request.TokenReissueRequest
import com.epsilon.nagginggnome.domain.auth.dto.response.TokenReissueResponse
import com.epsilon.nagginggnome.domain.auth.repository.RefreshTokenRepository
import com.epsilon.nagginggnome.domain.user.repository.UserRepository
import com.epsilon.nagginggnome.global.constant.code.JwtErrorCode
import com.epsilon.nagginggnome.global.constant.code.UserErrorCode
import com.epsilon.nagginggnome.global.exception.ApiException
import com.epsilon.nagginggnome.global.security.crypto.TokenHasher
import com.epsilon.nagginggnome.global.security.jwt.JwtProvider
import com.epsilon.nagginggnome.global.security.jwt.JwtUtils
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class AuthService(
    private val userRepository: UserRepository,
    private val refreshTokenRepository: RefreshTokenRepository,
    private val jwtUtils: JwtUtils,
    private val jwtProvider: JwtProvider,
    private val tokenHasher: TokenHasher
) {

    /**
     * Access Token 재발급
     *
     * 처리 흐름
     * - refreshToken 서명 검증 및 만료 검증
     * - refreshToken 타입 검증
     * - subject(userId) 추출
     * - Redis 저장 해시와 요청 토큰 해시 비교
     * - role 조회 후 accessToken 발급
     */
    @Transactional(readOnly = true)
    fun reissue(req: TokenReissueRequest): TokenReissueResponse {
        val refreshToken = req.refreshToken

        // JWT 파싱 과정에서 서명/만료 검증이 수행
        val claims = jwtUtils.extractClaims(refreshToken)

        // refresh 토큰만 허용
        if (!jwtUtils.isRefreshToken(claims)) {
            throw ApiException(JwtErrorCode.INVALID_TOKEN_TYPE)
        }

        // subject에서 userId(UUID)를 추출
        val userId = jwtUtils.extractSubject(claims)

        // 토큰 검증
        validateRefreshToken(userId, refreshToken)

        val user = userRepository.findByIdOrNull(userId)
            ?: throw ApiException(UserErrorCode.USER_NOT_FOUND)

        // Access Token  재발급
        val issuedAccess = jwtProvider.generateAccessToken(userId, user.role)

        return TokenReissueResponse(
            accessToken = issuedAccess.token
        )
    }

    /**
     * 멱등 로그아웃
     *
     * 처리 흐름
     * - refreshToken 서명 검증 및 만료 검증
     * - refresh 토큰 타입 검증
     * - subject(userId) 추출
     * - Redis 저장 해시와 비교 후 일치 시 삭제
     */
    fun logout(req: LogoutRequest) {
        val refreshToken = req.refreshToken

        // JWT 파싱(서명/만료 검증) 실패면 종료
        val claims = runCatching { jwtUtils.extractClaims(refreshToken) }
            .getOrNull()
            ?: return

        // refresh 토큰이 아니면 종료
        if (!jwtUtils.isRefreshToken(claims)) return

        // subject(userId) 추출 실패면 종료
        val userId = runCatching { jwtUtils.extractSubject(claims) }
            .getOrNull()
            ?: return

        //  Redis 저장 해시가 없으면 이미 로그아웃으로 보고 종료
        val savedHash = refreshTokenRepository.find(userId) ?: return

        if (tokenHasher.match(refreshToken, savedHash)) {
            refreshTokenRepository.delete(userId)
        }
    }

    /**
     * Refresh Token이 서버에 저장된 값과 일치하는지 검증
     */
    private fun validateRefreshToken(userId: UUID, refreshToken: String) {

        // Redis 저장 해시 조회
        val savedHash = refreshTokenRepository.find(userId)
            ?: throw ApiException(JwtErrorCode.REFRESH_TOKEN_NOT_FOUND)

        if (!tokenHasher.match(refreshToken, savedHash)) {
            refreshTokenRepository.delete(userId)
            throw ApiException(JwtErrorCode.INVALID_REFRESH_TOKEN)
        }
    }
}
