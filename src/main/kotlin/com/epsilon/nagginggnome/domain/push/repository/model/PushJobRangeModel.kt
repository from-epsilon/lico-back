package com.epsilon.nagginggnome.domain.push.repository.model

import java.time.Instant

/**
 * Push Job 범위 모델
 */
data class PushJobRangeModel(
    /**
     * 시작 시각
     */
    val from: Instant,

    /**
     * 종료 시각
     */
    val to: Instant
)
