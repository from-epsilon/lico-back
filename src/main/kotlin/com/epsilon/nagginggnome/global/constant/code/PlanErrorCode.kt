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

    /**
     * 플랜을 찾지 못한 경우
     */
    PLAN_NOT_FOUND(
        code = "PLAN_404_NOT_FOUND",
        status = HttpStatus.NOT_FOUND,
        message = "Plan not found."
    ),

    /**
     * 플랜의 현재 스냅샷 포인터가 비정상인 경우
     */
    PLAN_INVALID_STATE(
        code = "PLAN_409_INVALID_STATE",
        status = HttpStatus.CONFLICT,
        message = "Plan is in an invalid state."
    )
}
