package com.epsilon.nagginggnome.global.constant.code

import org.springframework.http.HttpStatus

/**
 * 실패 코드 규격
 */
interface ErrorCode {
    val code: String
    val status: HttpStatus
    val message: String
}
