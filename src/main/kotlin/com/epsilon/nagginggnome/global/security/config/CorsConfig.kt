package com.epsilon.nagginggnome.global.security.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource

@Configuration
class CorsConfig {

    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource {
        val config = CorsConfiguration()

        /**
         * 허용 Origin 패턴
         */
        config.allowedOriginPatterns = listOf("*")

        /**
         * 허용 HTTP 메서드
         */
        config.allowedMethods = listOf("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")

        /**
         * 허용 헤더
         */
        config.allowedHeaders = listOf("Authorization", "Content-Type", "X-Requested-With")

        /**
         * 클라이언트가 읽어야 하는 응답 헤더가 있으면 노출
         */
        config.exposedHeaders = listOf("Authorization")

        /**
         * 쿠키 기반 인증을 쓸 때만 true
         */
        config.allowCredentials = false

        /**
         * 프리플라이트 캐시 시간
         */
        config.maxAge = 3600

        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", config)
        return source
    }
}
