package com.epsilon.nagginggnome.global.security.jwt

object JwtConstants {
    const val AUTHORIZATION_HEADER: String = "Authorization"
    const val BEARER_PREFIX: String = "Bearer "

    const val CLAIM_ROLE: String = "role"
    const val CLAIM_TYPE: String = "type"

    const val TOKEN_TYPE_ACCESS: String = "access"
    const val TOKEN_TYPE_REFRESH: String = "refresh"
}
