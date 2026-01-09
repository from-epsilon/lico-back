package com.epsilon.nagginggnome.global.constant.code

import org.springframework.http.HttpStatus

enum class CommonErrorCode(
    override val code: String,
    override val status: HttpStatus,
    override val message: String
) : ErrorCode {

    /**
     * 요청 값이 유효하지 않거나 요청 형식이 올바르지 않은 경우
     */
    BAD_REQUEST("COMMON_400", HttpStatus.BAD_REQUEST, "Bad Request"),

    /**
     * 인증에 실패한 경우
     */
    UNAUTHORIZED("COMMON_401", HttpStatus.UNAUTHORIZED, "Unauthorized"),

    /**
     * 인증은 되었으나 접근 권한이 없는 경우
     */
    FORBIDDEN("COMMON_403", HttpStatus.FORBIDDEN, "Forbidden"),

    /**
     * 요청한 리소스를 찾을 수 없는 경우
     */
    NOT_FOUND("COMMON_404", HttpStatus.NOT_FOUND, "Not Found"),

    /**
     * 서버 내부 처리 중 예기치 못한 오류가 발생한 경우
     */
    INTERNAL_ERROR("COMMON_500", HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error")
}
