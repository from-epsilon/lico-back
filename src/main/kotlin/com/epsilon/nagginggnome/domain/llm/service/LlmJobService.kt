package com.epsilon.nagginggnome.domain.llm.service

import com.epsilon.nagginggnome.domain.llm.constant.LlmJobStatus
import com.epsilon.nagginggnome.domain.llm.constant.LlmJobType
import com.epsilon.nagginggnome.domain.llm.dto.request.UserSummaryJobInput
import com.epsilon.nagginggnome.domain.llm.repository.LlmJobRepository
import com.epsilon.nagginggnome.domain.llm.repository.model.LlmJobApplyModel
import com.epsilon.nagginggnome.domain.llm.repository.model.LlmJobCreateModel
import com.epsilon.nagginggnome.domain.push.dto.request.PushBatchUpsertRequest
import com.epsilon.nagginggnome.domain.push.service.PushJobService
import com.epsilon.nagginggnome.domain.user.config.UserProperties
import com.epsilon.nagginggnome.domain.user.repository.UserSummaryRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import tools.jackson.databind.ObjectMapper
import java.time.Duration
import java.time.Instant

@Service
class LlmJobService(
    private val llmJobRepository: LlmJobRepository,
    private val userSummaryRepository: UserSummaryRepository,
    private val userProperties: UserProperties,
    private val pushJobService: PushJobService,
    private val objectMapper: ObjectMapper
) {

    fun enqueueUserSummaries(now: Instant, limit: Int) {
        val summaryCutoff = now.minus(Duration.ofDays(14))
        val lastLoginCutoff = now.minus(Duration.ofDays(userProperties.dormancyLastLoginDays))
        val userIds = userSummaryRepository.findUserIdsDueForSummary(
            summaryCutoff = summaryCutoff,
            lastLoginCutoff = lastLoginCutoff,
            limit = limit
        )
        if (userIds.isEmpty()) return

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

    @Transactional
    fun applySuccessJobs(now: Instant, limit: Int) {
        applyJobs(now, llmJobRepository.lockNextSuccessUnapplied(limit))
    }

    @Transactional
    fun applySuccessJobById(now: Instant, id: Long) {
        llmJobRepository.lockSuccessUnappliedById(id)?.let { job ->
            applyJobs(now, listOf(job))
        }
    }

    private fun applyJobs(now: Instant, jobs: List<LlmJobApplyModel>) {
        jobs.mapNotNull { job ->
            runCatching { applyJob(job) }.getOrNull()?.let { job.id }
        }.takeIf { it.isNotEmpty() }
            ?.let { llmJobRepository.markApplied(it, now) }
    }

    private fun applyJob(job: LlmJobApplyModel) {
        when (job.type) {
            LlmJobType.USER_SUMMARY -> {
                val input = objectMapper.readValue(job.inputJson, UserSummaryJobInput::class.java)
                userSummaryRepository.insertSummary(
                    userId = input.userId,
                    summaryJson = job.outputJson
                )
            }

            LlmJobType.PUSH_BATCH -> {
                val output = objectMapper.readValue(job.outputJson, PushBatchUpsertRequest::class.java)
                pushJobService.pushBatchUpsert(userId = output.userId, req = output)
            }
        }
    }
}
