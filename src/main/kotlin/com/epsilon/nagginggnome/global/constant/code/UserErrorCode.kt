package com.epsilon.nagginggnome.global.constant.code

import org.springframework.http.HttpStatus

enum class UserErrorCode(
    override val code: String,
    override val status: HttpStatus,
    override val message: String
) : ErrorCode {

    /**
     * 유저를 찾지 못한 경우
     */
    USER_NOT_FOUND(
        code = "USER_404_NOT_FOUND",
        status = HttpStatus.NOT_FOUND,
        message = "User not found."
    ),
}
