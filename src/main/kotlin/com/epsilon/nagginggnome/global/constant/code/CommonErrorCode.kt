package com.epsilon.nagginggnome.global.constant.code

import org.springframework.http.HttpStatus

/**
 * 공통 에러 코드 정의
 */
enum class CommonErrorCode(
    override val code: String,
    override val status: HttpStatus,
    override val message: String
) : ErrorCode {

    /**
     * 요청 값이 유효하지 않거나 요청 형식이 올바르지 않은 경우
     */
    BAD_REQUEST(
        code = "COMMON_400",
        status = HttpStatus.BAD_REQUEST,
        message = "Bad Request"
    ),

    /**
     * 인증에 실패한 경우
     */
    UNAUTHORIZED(
        code = "COMMON_401",
        status = HttpStatus.UNAUTHORIZED,
        message = "Unauthorized"
    ),

    /**
     * 인증은 되었으나 접근 권한이 없는 경우
     */
    FORBIDDEN(
        code = "COMMON_403",
        status = HttpStatus.FORBIDDEN,
        message = "Forbidden"
    ),

    /**
     * 요청한 리소스를 찾을 수 없는 경우
     */
    NOT_FOUND(
        code = "COMMON_404",
        status = HttpStatus.NOT_FOUND,
        message = "Not Found"
    ),

    /**
     * 동일 리소스가 이미 존재하여 요청을 처리할 수 없는 경우
     */
    CONFLICT(
        code = "COMMON_409",
        status = HttpStatus.CONFLICT,
        message = "Conflict"
    ),

    /**
     * 서버 내부 처리 중 예기치 못한 오류가 발생한 경우
     */
    INTERNAL_SERVER_ERROR(
        code = "COMMON_500",
        status = HttpStatus.INTERNAL_SERVER_ERROR,
        message = "Internal Server Error"
    )
}
