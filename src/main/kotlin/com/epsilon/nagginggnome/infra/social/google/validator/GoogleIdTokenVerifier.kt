package com.epsilon.nagginggnome.infra.social.google.validator

import com.epsilon.nagginggnome.global.constant.code.CommonErrorCode
import com.epsilon.nagginggnome.global.exception.ApiException
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.security.oauth2.jwt.JwtDecoder
import org.springframework.security.oauth2.jwt.JwtException
import org.springframework.stereotype.Component

/**
 * Google ID Token 검증 컴포넌트
 */
@Component
class GoogleIdTokenVerifier(
    private val googleJwtDecoder: JwtDecoder
) {

    /**
     * Google ID Token을 검증하고 Jwt를 반환
     */
    fun verifyAndDecode(idToken: String): Jwt {

        if (idToken.isBlank()) {
            throw ApiException(CommonErrorCode.BAD_REQUEST)
        }

        return try {
            googleJwtDecoder.decode(idToken)
        } catch (e: JwtException) {
            throw ApiException(
                errorCode = CommonErrorCode.UNAUTHORIZED
            )
        } catch (e: IllegalArgumentException) {
            throw ApiException(
                errorCode = CommonErrorCode.UNAUTHORIZED
            )
        }
    }
}
