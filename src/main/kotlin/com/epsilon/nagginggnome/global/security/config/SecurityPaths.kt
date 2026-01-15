package com.epsilon.nagginggnome.global.security.config

/**
 * Security에서 사용할 엔드포인트 패턴
 */
enum class SecurityPaths(
    private val rawPattern: String,
    private val permitAll: Boolean
) {

    /**
     * 로그아웃 API
     */
    AUTH_LOGOUT("/v1/auth/logout", true),
    
    /**
     * 토큰 재발급 API
     */
    AUTH_TOKEN_REISSUE("/v1/auth/token/reissue", true),

    /**
     * 소셜 인증 API
     */
    AUTH_SOCIAL("/v1/auth/social/**", true),

    /**
     * 에러 엔드포인트
     */
    ERROR("/error", true);

    fun pattern(): String = rawPattern

    companion object {

        /**
         * permitAll 대상 패턴들을 한 번에 반환
         */
        fun permitAllPatterns(): Array<String> {
            return entries
                .filter { it.permitAll }
                .map { it.pattern() }
                .toTypedArray()
        }
    }
}
