package com.epsilon.nagginggnome.domain.push.constant

/**
 * 푸시 유형
 */
enum class PushType {
    REMINDER,
    ADDITIONAL;

    companion object {
        /**
         * 문자열을 PushType로 안전 변환
         */
        fun from(value: String?): PushType? =
            value
                ?.trim()
                ?.uppercase()
                ?.let { normalized -> PushType.entries.firstOrNull { it.name == normalized } }
    }
}
