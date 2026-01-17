package com.epsilon.nagginggnome.global.constant.code

import org.springframework.http.HttpStatus

enum class PlanErrorCode(
    override val code: String,
    override val status: HttpStatus,
    override val message: String
) : ErrorCode {

    /**
     * 플랜 생성 처리 중 실패한 경우
     */
    PLAN_CREATE_FAILED(
        code = "PLAN_500_CREATE_FAILED",
        status = HttpStatus.INTERNAL_SERVER_ERROR,
        message = "Failed to create plan."
    ),
}
