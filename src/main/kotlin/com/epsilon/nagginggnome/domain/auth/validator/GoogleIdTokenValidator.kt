package com.epsilon.nagginggnome.domain.auth.validator

import org.springframework.security.oauth2.core.OAuth2Error
import org.springframework.security.oauth2.core.OAuth2TokenValidator
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult
import org.springframework.security.oauth2.jwt.Jwt

/**
 * Google ID Token 검증 컴포넌트
 */
class GoogleIdTokenValidator(
    private val allowedIssuers: Set<String>,
    private val expectedClientId: String
) : OAuth2TokenValidator<Jwt> {

    override fun validate(token: Jwt): OAuth2TokenValidatorResult {
        validateIssuer(token)?.let { return OAuth2TokenValidatorResult.failure(it) }
        validateAudience(token)?.let { return OAuth2TokenValidatorResult.failure(it) }
        return OAuth2TokenValidatorResult.success()
    }

    /**
     * issuer(iss) 검증
     *
     * 검증 정책
     * - token.issuer 값이 allowedIssuers 중 하나와 정확히 일치해야 성공
     */
    private fun validateIssuer(token: Jwt): OAuth2Error? {
        val issuer = token.issuer?.toString().orEmpty()

        if (issuer.isNotBlank() && allowedIssuers.contains(issuer)) {
            return null
        }

        return OAuth2Error(
            "invalid_token",
            "Invalid issuer(iss). issuer=$issuer",
            null
        )
    }

    /**
     * audience(aud) 검증
     *
     * 검증 정책
     * - token.audience 리스트 안에 expectedClientId가 포함되어야 성공
     */
    private fun validateAudience(token: Jwt): OAuth2Error? {
        val audiences = token.audience ?: emptyList()

        if (audiences.contains(expectedClientId)) {
            return null
        }

        return OAuth2Error(
            "invalid_token",
            "Invalid audience(aud). aud=$audiences",
            null
        )
    }
}
