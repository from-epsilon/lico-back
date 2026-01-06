package com.epsilon.nagginggnome.global.code

import org.springframework.http.HttpStatus

enum class CommonErrorCode(
    override val code: String,
    override val status: HttpStatus,
    override val message: String
) : ErrorCode {

    /**
     * 요청 형식 오류
     */
    INVALID_REQUEST("COMMON_400", HttpStatus.BAD_REQUEST, "Bad Request"),

    /**
     * 인증 오류
     */
    UNAUTHORIZED("COMMON_401", HttpStatus.UNAUTHORIZED, "Unauthorized"),

    /**
     * 권한 오류
     */
    FORBIDDEN("COMMON_403", HttpStatus.FORBIDDEN, "Forbidden"),

    /**
     * 서버 내부 오류
     */
    INTERNAL_ERROR("COMMON_500", HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error")
}
