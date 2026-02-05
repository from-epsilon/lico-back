package com.epsilon.nagginggnome.domain.llm.constant

/**
 * LLM 메타 데이터 키
 */
enum class LlmMetaData(val key: String) {
    MODEL("model"),
    TOKEN_USAGE("token_usage"),
    GENERATED_AT("generated_at"),
    INTENT("intent")
}
