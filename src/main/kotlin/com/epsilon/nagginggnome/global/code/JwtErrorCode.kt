package com.epsilon.nagginggnome.global.code

import org.springframework.http.HttpStatus

/**
 * JWT 관련 에러 코드 정의
 */
enum class JwtErrorCode(
    override val code: String,
    override val status: HttpStatus,
    override val message: String
) : ErrorCode {

    /**
     * 지원하지 않는 Token 형식인 경우
     */
    UNSUPPORTED_TOKEN(
        code = "JWT_400_UNSUPPORTED_TOKEN",
        status = HttpStatus.BAD_REQUEST,
        message = "Unsupported token format."
    ),

    /**
     * Token이 없거나 Authorization 헤더 형식이 잘못된 경우
     */
    EMPTY_TOKEN(
        code = "JWT_400_EMPTY_TOKEN",
        status = HttpStatus.BAD_REQUEST,
        message = "Token is missing or has an invalid format."
    ),

    /**
     * Token이 만료된 경우입니다.
     */
    EXPIRED_TOKEN(
        code = "JWT_401_EXPIRED_TOKEN",
        status = HttpStatus.UNAUTHORIZED,
        message = "Token has expired."
    ),

    /**
     * Token이 유효하지 않은 경우
     */
    INVALID_TOKEN_TYPE(
        code = "JWT_401_INVALID_TOKEN_TYPE",
        status = HttpStatus.UNAUTHORIZED,
        message = "Invalid token type."
    ),

    /**
     * Token이 유효하지 않은 경우
     */
    INVALID_TOKEN_CLAIM(
        code = "JWT_401_INVALID_TOKEN_CLAIM",
        status = HttpStatus.UNAUTHORIZED,
        message = "Invalid token claim."
    ),
    
    /**
     * Token 서명이 유효하지 않은 경우
     */
    INVALID_TOKEN_SIGNATURE(
        code = "JWT_401_INVALID_TOKEN_SIGNATURE",
        status = HttpStatus.UNAUTHORIZED,
        message = "Invalid token signature."
    ),

    /**
     * 서버 저장소에 Refresh Token이 존재하지 않는 경우
     */
    REFRESH_TOKEN_NOT_FOUND(
        code = "JWT_401_REFRESH_TOKEN_NOT_FOUND",
        status = HttpStatus.UNAUTHORIZED,
        message = "Refresh token not found on the server."
    ),

    /**
     * Refresh Token 자체가 유효하지 않은 경우
     */
    INVALID_REFRESH_TOKEN(
        code = "JWT_401_INVALID_REFRESH_TOKEN",
        status = HttpStatus.UNAUTHORIZED,
        message = "Invalid refresh token."
    )
}
