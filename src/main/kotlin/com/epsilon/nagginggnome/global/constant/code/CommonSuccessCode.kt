package com.epsilon.nagginggnome.global.constant.code

/**
 * 공통 성공 코드 정의
 */
enum class CommonSuccessCode(
    override val code: String,
    override val message: String
) : SuccessCode {

    /**
     * 기본 성공 코드
     */
    SUCCESS("SUCCESS", "Success"),

    /**
     * 생성 성공 코드
     */
    CREATED("CREATED", "Created")
}
