package com.epsilon.nagginggnome.global.constant.code

import org.springframework.http.HttpStatus

/**
 * 푸시 작업 관련 에러 코드 정의
 */
enum class PushJobErrorCode(
    override val code: String,
    override val status: HttpStatus,
    override val message: String
) : ErrorCode {

    /**
     * kind 값이 허용된 enum 범위(REMIND, GENERAL)에 없는 경우
     */
    INVALID_MESSAGE_KIND(
        code = "PUSHJOB_400_INVALID_MESSAGE_KIND",
        status = HttpStatus.BAD_REQUEST,
        message = "Invalid message kind."
    ),

    /**
     * 대상 사용자에 대한 FCM 토큰을 찾지 못한 경우
     */
    FCM_TOKEN_NOT_FOUND(
        code = "PUSHJOB_404_FCM_TOKEN_NOT_FOUND",
        status = HttpStatus.NOT_FOUND,
        message = "FCM token not found."
    ),
}
