package com.epsilon.nagginggnome.domain.user.repository

import java.time.Instant
import java.util.UUID

interface UserTargetRepository {
    fun findAdditionalTargetUserIds(lastLoginCutoff: Instant, limit: Int): List<UUID>
}
