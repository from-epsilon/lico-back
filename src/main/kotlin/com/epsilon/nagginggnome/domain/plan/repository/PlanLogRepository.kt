package com.epsilon.nagginggnome.domain.plan.repository

import java.util.UUID

interface PlanLogRepository {
    fun insertIfAbsent(planId: UUID, userId: UUID): Int
}
