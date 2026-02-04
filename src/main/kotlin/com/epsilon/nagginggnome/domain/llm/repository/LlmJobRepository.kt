package com.epsilon.nagginggnome.domain.llm.repository

import com.epsilon.nagginggnome.domain.llm.repository.model.LlmJobCreateModel

interface LlmJobRepository {
    fun insertLlmJobs(models: List<LlmJobCreateModel>): Int
}
