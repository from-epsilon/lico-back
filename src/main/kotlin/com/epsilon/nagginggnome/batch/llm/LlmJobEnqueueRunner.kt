package com.epsilon.nagginggnome.batch.llm

import com.epsilon.nagginggnome.domain.llm.constant.LlmJobStatus
import com.epsilon.nagginggnome.domain.llm.constant.LlmJobType
import com.epsilon.nagginggnome.domain.llm.dto.request.UserSummaryJobInput
import com.epsilon.nagginggnome.domain.llm.repository.LlmJobRepository
import com.epsilon.nagginggnome.domain.llm.repository.model.LlmJobCreateModel
import com.epsilon.nagginggnome.domain.user.repository.UserSummaryRepository
import org.springframework.stereotype.Service
import tools.jackson.databind.ObjectMapper
import java.time.Duration
import java.time.Instant

@Service
class LlmJobEnqueueRunner(
    private val userSummaryRepository: UserSummaryRepository,
    private val llmJobRepository: LlmJobRepository,
    private val objectMapper: ObjectMapper
) {

    /**
     * USER_SUMMARY 작업 enqueue
     */
    fun runOnce(now: Instant, limit: Int) {
        val cutoff = now.minus(Duration.ofDays(14))
        val userIds = userSummaryRepository.findUserIdsDueForSummary(cutoff, limit)
        if (userIds.isEmpty()) {
            return
        }

        val models = userIds.map { userId ->
            LlmJobCreateModel(
                type = LlmJobType.USER_SUMMARY,
                status = LlmJobStatus.PENDING,
                inputJson = objectMapper.writeValueAsString(
                    UserSummaryJobInput(userId = userId)
                )
            )
        }

        llmJobRepository.insertLlmJobs(models)
    }
}
