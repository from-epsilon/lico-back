package com.epsilon.nagginggnome.domain.llm.repository.model

import com.epsilon.nagginggnome.domain.llm.constant.LlmJobType

/**
 * llm_jobs apply 대상 모델
 */
data class LlmJobApplyModel(
    val id: Long,
    val type: LlmJobType,
    val inputJson: String,
    val outputJson: String
)
