package com.epsilon.nagginggnome.domain.llm.repository.model

import com.epsilon.nagginggnome.domain.llm.constant.LlmJobStatus
import com.epsilon.nagginggnome.domain.llm.constant.LlmJobType

/**
 * llm_jobs insert 모델
 */
data class LlmJobCreateModel(
    val type: LlmJobType,
    val status: LlmJobStatus,
    val inputJson: String
)
