package com.epsilon.nagginggnome.global.constant.code

import org.springframework.http.HttpStatus

/**
 * 메시지 관련 에러 코드 정의
 */
enum class MessageErrorCode(
    override val code: String,
    override val status: HttpStatus,
    override val message: String
) : ErrorCode {

    /**
     * 클라이언트 메시지 ID가 이미 존재하는 경우
     */
    DUPLICATE_MESSAGE_ID(
        code = "MESSAGE_409_DUPLICATE_ID",
        status = HttpStatus.CONFLICT,
        message = "Message with this ID already exists."
    ),

    /**
     * 메시지를 찾을 수 없는 경우
     */
    MESSAGE_NOT_FOUND(
        code = "MESSAGE_404_NOT_FOUND",
        status = HttpStatus.NOT_FOUND,
        message = "Message not found."
    ),

    /**
     * 서버 메시지를 찾을 수 없는 경우
     */
    SERVER_MESSAGE_NOT_FOUND(
        code = "MESSAGE_404_SERVER_MESSAGE_NOT_FOUND",
        status = HttpStatus.NOT_FOUND,
        message = "Server message not found."
    ),
}
