package com.epsilon.nagginggnome.domain.auth.config

import com.epsilon.nagginggnome.domain.auth.validator.GoogleIdTokenValidator
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator
import org.springframework.security.oauth2.jwt.JwtDecoder
import org.springframework.security.oauth2.jwt.JwtDecoders
import org.springframework.security.oauth2.jwt.JwtValidators
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder

/**
 * Google ID Token(JWT) 검증용 JwtDecoder 설정 클래스
 */
@Configuration
class GoogleJwtDecoderConfig(
    private val props: GoogleSocialProperties
) {

    /**
     * Google ID Token을 검증하는 JwtDecoder Bean
     *
     * 처리 흐름
     * - issuer 기반 OIDC Discovery로 JWKS(공개키) 정보를 자동 조회하여 서명 검증
     * - 기본 시간 검증(exp, nbf 등) 수행
     * - Google 전용 검증(iss, aud) 수행
     */
    @Bean
    fun googleJwtDecoder(props: GoogleSocialProperties): JwtDecoder {
        val decoder = JwtDecoders.fromIssuerLocation<NimbusJwtDecoder>(props.discoveryIssuer)

        // exp(만료), nbf(활성 시작) 등 표준 시간 검증을 수행하는 Validator
        val timeValidator = JwtValidators.createDefault()

        // iss(발급자), aud(대상) 검증을 수행하는 Google 전용 Validator
        val googleValidator = GoogleIdTokenValidator(
            allowedIssuers = props.allowedIssuers.toSet(),
            expectedClientId = props.clientId
        )

        decoder.setJwtValidator(
            DelegatingOAuth2TokenValidator(
                timeValidator,
                googleValidator
            )
        )

        return decoder
    }
}
