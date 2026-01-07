package com.epsilon.nagginggnome.global.security.jwt

import com.epsilon.nagginggnome.domain.user.constant.Role
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import org.springframework.stereotype.Component
import java.time.Instant
import java.util.*
import javax.crypto.SecretKey

/**
 * JWT 생성 및 검증을 담당하는 컴포넌트
 */
@Component
class JwtTokenProvider(
    private val props: JwtProperties
) {
    private val key: SecretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(props.secret))

    /**
     * Access Token 생성
     */
    fun generateAccessToken(
        userId: UUID,
        role: Role
    ): String {
        val now = Instant.now()
        val exp = now.plusSeconds(props.accessTokenExp)

        return Jwts.builder()
            .issuer(props.issuer)
            .subject(userId.toString())
            .issuedAt(Date.from(now))
            .expiration(Date.from(exp))
            .claim(JwtConstants.CLAIM_ROLE, role.name)
            .claim(JwtConstants.CLAIM_TYPE, JwtConstants.TOKEN_TYPE_ACCESS)
            .signWith(key)
            .compact()
    }

    /**
     * Refresh Token 생성
     */
    fun generateRefreshToken(
        userId: UUID
    ): String {
        val now = Instant.now()
        val exp = now.plusSeconds(props.refreshTokenExp)

        return Jwts.builder()
            .issuer(props.issuer)
            .subject(userId.toString())
            .issuedAt(Date.from(now))
            .expiration(Date.from(exp))
            .claim(JwtConstants.CLAIM_TYPE, JwtConstants.TOKEN_TYPE_REFRESH)
            .signWith(key)
            .compact()
    }
}
