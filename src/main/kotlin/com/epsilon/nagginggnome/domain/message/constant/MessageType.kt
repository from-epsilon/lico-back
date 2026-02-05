package com.epsilon.nagginggnome.domain.message.constant

/**
 * 서버 메시지 타입
 */
enum class MessageType {
    REMINDER,
    ADDITIONAL;

    companion object {
        /**
         * 문자열을 MessageType으로 안전 변환
         */
        fun from(value: String?): MessageType? =
            value
                ?.trim()
                ?.uppercase()
                ?.let { normalized ->
                    when (normalized) {
                        "REMIND" -> REMINDER
                        "GENERAL" -> ADDITIONAL
                        else -> MessageType.entries.firstOrNull { it.name == normalized }
                    }
                }
    }
}
