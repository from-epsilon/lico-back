package com.epsilon.nagginggnome.global.security.jwt.filter

import com.epsilon.nagginggnome.domain.user.constant.Role
import com.epsilon.nagginggnome.global.constant.code.JwtErrorCode
import com.epsilon.nagginggnome.global.exception.ApiException
import com.epsilon.nagginggnome.global.security.jwt.JwtConstants
import com.epsilon.nagginggnome.global.security.jwt.JwtUtils
import io.jsonwebtoken.Claims
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import java.util.*

/**
 * 매 요청마다 Authorization 헤더의 Bearer 토큰을 검사하여 인증을 구성하는 필터
 *
 * subject + roles로 Authentication을 만들어 SecurityContext에 저장
 */
@Component
class JwtAuthenticationFilter(
    private val jwtUtils: JwtUtils
) : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val header: String = request.getHeader(JwtConstants.AUTHORIZATION_HEADER) ?: run {
            filterChain.doFilter(request, response)
            return
        }

        val token: String = jwtUtils.extractToken(header)
        val claims: Claims = jwtUtils.extractClaims(token)

        if (!jwtUtils.isAccessToken(claims)) {
            throw ApiException(JwtErrorCode.INVALID_TOKEN_TYPE)
        }

        val subject: UUID = jwtUtils.extractSubject(claims)
        val role: Role = jwtUtils.extractRole(claims)

        val authorities = listOf(SimpleGrantedAuthority(role.toAuthority()))

        val authentication = UsernamePasswordAuthenticationToken(subject, null, authorities).apply {
            details = WebAuthenticationDetailsSource().buildDetails(request)
        }
        SecurityContextHolder.getContext().authentication = authentication

        filterChain.doFilter(request, response)
    }
}
