package com.epsilon.nagginggnome.domain.plan.repository

import java.util.UUID

interface PlanLogRepository {
    fun insertIfAbsent(planId: UUID, userId: UUID): Int
    fun appendRecentLog(planId: UUID, logJson: String): Int
    fun findRecentLogs(planId: UUID): List<String>
    fun removeOldestRecentLogs(planId: UUID, count: Int): Int
    fun updateCompaction(planId: UUID, compaction: String): Int
}
