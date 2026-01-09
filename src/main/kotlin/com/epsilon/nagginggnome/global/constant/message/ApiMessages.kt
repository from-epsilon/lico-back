package com.epsilon.nagginggnome.global.constant.message

/**
 * API 응답 메시지 상수
 *
 * 목적
 * - 매직 문자열 분산 방지
 * - 메시지 변경 시 단일 지점 수정
 *
 * 주의
 * - i18n이 필요해지면 MessageSource로 전환
 */
object ApiMessages {

    /**
     * 요청 본문 JSON 자체가 유효하지 않을 때 사용하는 메시지
     */
    const val INVALID_JSON_BODY: String = "Invalid JSON request body."

    /**
     * 요청 값의 형식이 올바르지 않을 때 사용하는 기본 메시지
     */
    const val INVALID_VALUE_FORMAT: String = "Invalid request value format."

    /**
     * 검증 실패가 발생했을 때 사용하는 상위 메시지
     */
    const val VALIDATION_FAILED: String = "Validation failed."

    /**
     * 특정 필드가 잘못된 경우 메시지 템플릿
     */
    private const val INVALID_FIELD_TEMPLATE: String = "Invalid value for field: `%s`."

    /**
     * 필드명을 포함한 메시지를 생성
     */
    fun invalidField(field: String): String = String.format(INVALID_FIELD_TEMPLATE, field)
}
