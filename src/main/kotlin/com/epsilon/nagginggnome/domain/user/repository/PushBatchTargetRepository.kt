package com.epsilon.nagginggnome.domain.user.repository

import com.epsilon.nagginggnome.domain.user.repository.model.PushBatchTarget
import java.time.Instant

interface PushBatchTargetRepository {
    fun findTargetsForPushBatch(lastLoginCutoff: Instant, limit: Int): List<PushBatchTarget>
}
