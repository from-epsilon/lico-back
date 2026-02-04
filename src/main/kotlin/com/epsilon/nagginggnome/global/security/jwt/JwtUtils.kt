package com.epsilon.nagginggnome.global.security.jwt

import com.epsilon.nagginggnome.domain.user.constant.Role
import com.epsilon.nagginggnome.global.constant.code.JwtErrorCode
import com.epsilon.nagginggnome.global.exception.ApiException
import io.jsonwebtoken.Claims
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.MalformedJwtException
import io.jsonwebtoken.UnsupportedJwtException
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import io.jsonwebtoken.security.SecurityException
import org.springframework.stereotype.Component
import java.util.UUID
import javax.crypto.SecretKey

/**
 * JWT 검증/추출을 담당하는 컴포넌트
 */
@Component
class JwtUtils(
    private val props: JwtProperties
) {
    private val key: SecretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(props.secret))
    private val parser = Jwts.parser()
        .verifyWith(key)
        .build()

    /**
     * Authorization 헤더에서 실제 Token 값만 추출
     */
    fun extractToken(header: String?): String =
        header?.takeIf { it.startsWith(JwtConstants.BEARER_PREFIX) }
            ?.substring(JwtConstants.BEARER_PREFIX.length)
            ?.trim()
            ?.takeIf { it.isNotEmpty() }
            ?: throw ApiException(JwtErrorCode.EMPTY_TOKEN)

    /**
     * Token 파싱 결과 Claims 반환
     */
    fun extractClaims(token: String): Claims =
        try {
            parser
                .parseSignedClaims(token)
                .payload
        } catch (e: SecurityException) {
            // 서명 검증 실패, 위변조 가능성, 보안 예외
            throw ApiException(JwtErrorCode.INVALID_TOKEN_SIGNATURE)
        } catch (e: MalformedJwtException) {
            // JWT 문자열 구조 자체가 깨진 경우
            throw ApiException(JwtErrorCode.INVALID_TOKEN_SIGNATURE)
        } catch (e: ExpiredJwtException) {
            // exp 기준으로 만료된 Token
            throw ApiException(JwtErrorCode.EXPIRED_TOKEN)
        } catch (e: UnsupportedJwtException) {
            // 지원하지 않는 JWT 포맷 또는 타입
            throw ApiException(JwtErrorCode.UNSUPPORTED_TOKEN)
        } catch (e: IllegalArgumentException) {
            // token이 빈 문자열 등으로 파서가 입력 자체를 거부
            throw ApiException(JwtErrorCode.EMPTY_TOKEN)
        }

    /**
     * Token에서 subject(UUID)를 추출
     */
    fun extractSubject(claims: Claims): UUID {
        val subject: String = claims.subject
            ?.trim()
            ?.takeIf { it.isNotEmpty() }
            ?: throw ApiException(JwtErrorCode.INVALID_TOKEN_CLAIM)

        return runCatching { UUID.fromString(subject) }
            .getOrElse {
                throw ApiException(JwtErrorCode.INVALID_TOKEN_CLAIM)
            }
    }

    /**
     * Claims에서 단일 Role을 추출
     */
    fun extractRole(claims: Claims): Role {
        val roleValue: String = (claims[JwtConstants.CLAIM_ROLE] as? String)
            ?.trim()
            ?.takeIf { it.isNotEmpty() }
            ?: throw ApiException(JwtErrorCode.INVALID_TOKEN_CLAIM)

        return Role.from(roleValue)
            ?: throw ApiException(JwtErrorCode.INVALID_TOKEN_CLAIM)
    }

    /**
     * 주어진 Claims가 Access Token인지 확인
     */
    fun isAccessToken(claims: Claims): Boolean =
        (claims[JwtConstants.CLAIM_TYPE] as? String) == JwtConstants.TOKEN_TYPE_ACCESS

    /**
     * 주어진 Claims가 Refresh Token인지 확인
     */
    fun isRefreshToken(claims: Claims): Boolean =
        (claims[JwtConstants.CLAIM_TYPE] as? String) == JwtConstants.TOKEN_TYPE_REFRESH
}
