package com.epsilon.nagginggnome.domain.push.constant

/**
 * Push Job 상태
 */
enum class PushJobStatus {
    READY,
    RUNNING,
    DONE,
    FAILED;

    companion object {
        /**
         * 문자열을 PushJobStatus로 안전 변환
         */
        fun from(value: String?): PushJobStatus? =
            value
                ?.trim()
                ?.uppercase()
                ?.let { normalized -> PushJobStatus.entries.firstOrNull { it.name == normalized } }
    }
}
