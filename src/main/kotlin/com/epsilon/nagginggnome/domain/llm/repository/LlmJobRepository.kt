package com.epsilon.nagginggnome.domain.llm.repository

import com.epsilon.nagginggnome.domain.llm.repository.model.LlmJobApplyModel
import com.epsilon.nagginggnome.domain.llm.repository.model.LlmJobCreateModel
import java.time.Instant
import java.util.UUID

interface LlmJobRepository {
    fun insertLlmJob(model: LlmJobCreateModel): Int
    fun lockNextSuccessUnapplied(limit: Int): List<LlmJobApplyModel>
    fun lockSuccessUnappliedById(id: Long): LlmJobApplyModel?
    fun existsPendingCompaction(planId: UUID): Boolean
    fun markApplied(ids: List<Long>, appliedAt: Instant): Int
}
