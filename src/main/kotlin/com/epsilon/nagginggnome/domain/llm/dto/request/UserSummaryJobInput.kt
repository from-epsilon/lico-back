package com.epsilon.nagginggnome.domain.llm.dto.request

import java.util.UUID

/**
 * USER_SUMMARY 작업 input_json
 */
data class UserSummaryJobInput(
    val userId: UUID
)
