package com.epsilon.nagginggnome.domain.user.repository.model

import java.util.UUID

data class PushBatchTarget(
    val userId: UUID,
    val timezone: String
)
