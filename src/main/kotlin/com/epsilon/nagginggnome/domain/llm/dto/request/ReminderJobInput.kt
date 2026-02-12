package com.epsilon.nagginggnome.domain.llm.dto.request

import java.time.Instant
import java.util.UUID

/**
 * REMINDER 작업 input_json
 */
data class ReminderJobInput(
    val userId: UUID,
    val planId: UUID,
    val scheduledAt: Instant
)
