package com.epsilon.nagginggnome.global.constant.code

import org.springframework.http.HttpStatus

/**
 * 플랜 관련 에러 코드 정의
 */
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
     * 플랜 버전이 오래된 요청인 경우
     */
    PLAN_VERSION_STALE(
        code = "PLAN_409_VERSION_STALE",
        status = HttpStatus.CONFLICT,
        message = "Plan version is stale."
    ),

    /**
     * 플랜 버전에 공백이 있는 요청인 경우
     */
    PLAN_VERSION_GAP(
        code = "PLAN_409_VERSION_GAP",
        status = HttpStatus.CONFLICT,
        message = "Plan version gap detected."
    ),
}
