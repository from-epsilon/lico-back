package com.epsilon.nagginggnome.domain.push.constant

/**
 * 메시지 종류
 */
enum class MessageKind {
    REMIND,
    GENERAL;

    companion object {
        /**
         * 문자열을 MessageKind로 안전 변환
         */
        fun from(value: String?): MessageKind? =
            value
                ?.trim()
                ?.uppercase()
                ?.let { normalized -> MessageKind.entries.firstOrNull { it.name == normalized } }
    }
}
