package com.epsilon.nagginggnome.domain.llm.repository

import com.epsilon.nagginggnome.domain.llm.repository.model.LlmJobApplyModel
import com.epsilon.nagginggnome.domain.llm.repository.model.LlmJobCreateModel
import java.time.Instant

interface LlmJobRepository {
    fun insertLlmJobs(models: List<LlmJobCreateModel>): Int
    fun lockNextSuccessUnapplied(limit: Int): List<LlmJobApplyModel>
    fun lockSuccessUnappliedById(id: Long): LlmJobApplyModel?
    fun markApplied(ids: List<Long>, appliedAt: Instant): Int
}
