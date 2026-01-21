package com.epsilon.nagginggnome.global.constant.code

import org.springframework.http.HttpStatus

/**
 * 유저 관련 에러 코드 정의
 */
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

    /**
     * 유저의 상세 정보를 찾지 못한 경우
     */
    USER_DETAIL_NOT_FOUND(
        code = "USER_DETAIL_404_NOT_FOUND",
        status = HttpStatus.NOT_FOUND,
        message = "User's detail not found."
    ),

    /**
     * 유저의 상세 정보가 존재하는데 생성하려는 경우
     */
    USER_DETAIL_ALREADY_EXISTS(
        code = "USER_DETAIL_409_CONFLICT",
        status = HttpStatus.CONFLICT,
        message = "User's detail already exists."
    )
}
