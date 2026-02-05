package com.epsilon.nagginggnome.domain.user.repository

import java.time.Instant
import java.util.UUID

interface UserSummaryRepository {
    fun findUserIdsDueForSummary(cutoff: Instant, limit: Int): List<UUID>
    fun insertSummary(userId: UUID, summaryJson: String): Int
}
