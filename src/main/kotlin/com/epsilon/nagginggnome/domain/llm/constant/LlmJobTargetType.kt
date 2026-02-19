package com.epsilon.nagginggnome.domain.llm.constant

/**
 * LLM 작업의 공통 대상 이벤트 타입
 */
enum class LlmJobTargetType {
    GNOME_MESSAGE,
    USER_MESSAGE,
    CREATE,
    UPDATE,
    DELETE
}
