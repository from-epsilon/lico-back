package com.epsilon.nagginggnome.domain.llm.service

import com.epsilon.nagginggnome.domain.llm.constant.LlmJobStatus
import com.epsilon.nagginggnome.domain.llm.constant.LlmJobType
import com.epsilon.nagginggnome.domain.llm.dto.request.UserSummaryJobInput
import com.epsilon.nagginggnome.domain.llm.repository.LlmJobRepository
import com.epsilon.nagginggnome.domain.llm.repository.model.LlmJobApplyModel
import com.epsilon.nagginggnome.domain.llm.repository.model.LlmJobCreateModel
import com.epsilon.nagginggnome.domain.user.repository.UserSummaryRepository
import org.springframework.stereotype.Service
import tools.jackson.databind.ObjectMapper
import java.time.Duration
import java.time.Instant

@Service
class LlmJobService(
    private val llmJobRepository: LlmJobRepository,
    private val userSummaryRepository: UserSummaryRepository,
    private val objectMapper: ObjectMapper
) {

    fun enqueueUserSummaries(now: Instant, limit: Int) {
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

    fun applySuccessJobs(now: Instant, limit: Int) {
        val jobs = llmJobRepository.lockNextSuccessUnapplied(limit)
        applyJobs(now, jobs)
    }

    fun applySuccessJobById(now: Instant, id: Long) {
        val job = llmJobRepository.lockSuccessUnappliedById(id) ?: return
        applyJobs(now, listOf(job))
    }

    private fun applyJobs(now: Instant, jobs: List<LlmJobApplyModel>) {
        if (jobs.isEmpty()) {
            return
        }

        val appliedIds = mutableListOf<Long>()

        jobs.forEach { job ->
            runCatching {
                when (job.type) {
                    LlmJobType.USER_SUMMARY -> {
                        val input = objectMapper.readValue(
                            job.inputJson,
                            UserSummaryJobInput::class.java
                        )
                        userSummaryRepository.insertSummary(
                            userId = input.userId,
                            summaryJson = job.outputJson
                        )
                        true
                    }
                    else -> false
                }
            }.onSuccess {
                if (it == true) {
                    appliedIds.add(job.id)
                }
            }
        }

        if (appliedIds.isNotEmpty()) {
            llmJobRepository.markApplied(appliedIds, now)
        }
    }
}
