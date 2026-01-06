package com.epsilon.nagginggnome.global.dto.response

import com.epsilon.nagginggnome.global.code.ErrorCode
import com.epsilon.nagginggnome.global.code.SuccessCode
import java.time.Instant

/**
 * 표준 API 응답 래퍼
 */
data class ApiResponse<T>(
    val success: Boolean,
    val code: String,
    val message: String,
    val data: T? = null,
    val timestamp: Instant = Instant.now()
) {
    companion object {
        
        /**
         * 성공 응답(데이터 포함)을 생성
         */
        fun <T> ok(
            successCode: SuccessCode,
            data: T,
            message: String = successCode.message
        ): ApiResponse<T> {
            return ApiResponse(
                success = true,
                code = successCode.code,
                message = message,
                data = data
            )
        }

        /**
         * 성공 응답(데이터 없음)을 생성
         */
        fun ok(
            successCode: SuccessCode,
            message: String = successCode.message
        ): ApiResponse<Unit> {
            return ApiResponse(
                success = true,
                code = successCode.code,
                message = message,
                data = Unit
            )
        }

        /**
         * 실패 응답을 생성
         */
        fun fail(
            errorCode: ErrorCode,
            message: String = errorCode.message
        ): ApiResponse<Nothing> {
            return ApiResponse(
                success = false,
                code = errorCode.code,
                message = message,
                data = null
            )
        }
    }
}
