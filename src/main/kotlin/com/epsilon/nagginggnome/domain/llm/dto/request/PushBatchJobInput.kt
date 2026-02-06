package com.epsilon.nagginggnome.domain.llm.dto.request

import java.time.Instant
import java.util.UUID

/**
 * PUSH_BATCH 작업 input_json
 */
data class PushBatchJobInput(
    val userId: UUID,
    val timeWindow: TimeWindow
) {
    data class TimeWindow(
        val start: Instant,
        val end: Instant
    )
}
