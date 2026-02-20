package com.epsilon.nagginggnome.domain.llm.service

import com.epsilon.nagginggnome.domain.llm.constant.LlmJobStatus
import com.epsilon.nagginggnome.domain.llm.constant.LlmJobType
import com.epsilon.nagginggnome.domain.llm.dto.request.CompactionJobInput
import com.epsilon.nagginggnome.domain.llm.dto.request.ReminderJobInput
import com.epsilon.nagginggnome.domain.llm.dto.request.UserSummaryJobInput
import com.epsilon.nagginggnome.domain.llm.dto.response.CompactionJobOutput
import com.epsilon.nagginggnome.domain.llm.dto.response.ReminderJobOutput
import com.epsilon.nagginggnome.domain.llm.repository.LlmJobRepository
import com.epsilon.nagginggnome.domain.llm.repository.model.LlmJobApplyModel
import com.epsilon.nagginggnome.domain.llm.repository.model.LlmJobCreateModel
import com.epsilon.nagginggnome.domain.push.service.PushJobService
import com.epsilon.nagginggnome.domain.push.util.PushRRuleUtils
import com.epsilon.nagginggnome.domain.plan.repository.PlanLogRepository
import com.epsilon.nagginggnome.domain.user.config.UserProperties
import com.epsilon.nagginggnome.domain.user.repository.UserSummaryRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import tools.jackson.databind.ObjectMapper
import java.time.Duration
import java.time.Instant
import java.time.ZoneId
import java.util.UUID

@Service
class LlmJobService(
    private val llmJobRepository: LlmJobRepository,
    private val userSummaryRepository: UserSummaryRepository,
    private val planLogRepository: PlanLogRepository,
    private val userProperties: UserProperties,
    private val pushJobService: PushJobService,
    private val objectMapper: ObjectMapper
) {

    fun enqueueCompactionIfNeeded(planId: UUID) {
        if (llmJobRepository.existsPendingCompaction(planId)) {
            return
        }

        val logs = planLogRepository.findRecentLogs(planId)
        if (logs.size < 50) {
            return
        }

        val compactionTarget = logs.take(30).map { logJson ->
            objectMapper.readValue(logJson, CompactionJobInput.CompactionTargetItem::class.java)
        }

        llmJobRepository.insertLlmJob(
            model = LlmJobCreateModel(
                type = LlmJobType.COMPACTION,
                status = LlmJobStatus.PENDING,
                inputJson = objectMapper.writeValueAsString(
                    CompactionJobInput(
                        planId = planId,
                        compactionTarget = compactionTarget
                    )
                )
            )
        )
    }

    fun enqueueUserSummaries(now: Instant, limit: Int) {
        val summaryCutoff = now.minus(Duration.ofDays(14))
        val lastLoginCutoff = now.minus(Duration.ofDays(userProperties.dormancyLastLoginDays))
        val userIds = userSummaryRepository.findUserIdsDueForSummary(
            summaryCutoff = summaryCutoff,
            lastLoginCutoff = lastLoginCutoff,
            limit = limit
        )
        if (userIds.isEmpty()) return

        userIds.forEach { userId ->
            llmJobRepository.insertLlmJob(
                model = LlmJobCreateModel(
                    type = LlmJobType.USER_SUMMARY,
                    status = LlmJobStatus.PENDING,
                    inputJson = objectMapper.writeValueAsString(
                        UserSummaryJobInput(
                            userId = userId,
                            flushTarget = emptyList()
                        )
                    )
                )
            )
        }
    }

    @Transactional
    fun enqueueReminderForPlan(
        now: Instant,
        userId: UUID,
        planId: UUID,
        timezone: String,
        rrule: String,
        dtstart: Instant,
        leadTime: Int?
    ) {
        pushJobService.deleteScheduledReminders(
            planId = planId,
            from = now
        )
        val scheduledAt = buildNextReminderAt(
            now = now,
            rrule = rrule,
            dtstart = dtstart,
            timezone = timezone,
            leadTime = leadTime
        ) ?: return
        llmJobRepository.insertLlmJob(
            model = LlmJobCreateModel(
                type = LlmJobType.REMINDER,
                status = LlmJobStatus.PENDING,
                inputJson = objectMapper.writeValueAsString(
                    ReminderJobInput(
                        userId = userId,
                        planId = planId,
                        scheduledAt = scheduledAt
                    )
                )
            )
        )
    }

    @Transactional
    fun applySuccessJobs(now: Instant, limit: Int) {
        applyJobs(
            now = now,
            jobs = llmJobRepository.lockNextSuccessUnapplied(
                limit = limit
            )
        )
    }

    @Transactional
    fun applySuccessJobById(now: Instant, id: Long) {
        llmJobRepository.lockSuccessUnappliedById(
            id = id
        )?.let { job ->
            applyJobs(
                now = now,
                jobs = listOf(job)
            )
        }
    }

    private fun applyJobs(now: Instant, jobs: List<LlmJobApplyModel>) {
        jobs.mapNotNull { job ->
            runCatching { applyJob(job = job) }.getOrNull()?.let { job.id }
        }.takeIf { it.isNotEmpty() }
            ?.let {
                llmJobRepository.markApplied(
                    ids = it,
                    appliedAt = now
                )
            }
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

            LlmJobType.REMINDER -> {
                val output = objectMapper.readValue(job.outputJson, ReminderJobOutput::class.java)
                pushJobService.insertReminder(
                    output = output
                )
            }

            LlmJobType.COMPACTION -> {
                val output = objectMapper.readValue(job.outputJson, CompactionJobOutput::class.java)
                planLogRepository.updateCompaction(
                    planId = output.request.planId,
                    compaction = output.compaction
                )
                planLogRepository.removeOldestRecentLogs(
                    planId = output.request.planId,
                    count = 30
                )
            }

            LlmJobType.ADDITIONAL -> {
                error("ADDITIONAL job apply is not implemented yet.")
            }
        }
    }

    private fun buildNextReminderAt(
        now: Instant,
        rrule: String,
        dtstart: Instant,
        timezone: String,
        leadTime: Int?
    ): Instant? {
        val leadTimeMinutes = leadTime ?: 0
        val zoneId = ZoneId.of(timezone)
        val after = now.plus(Duration.ofMinutes(leadTimeMinutes.toLong()))
        val occurrence = PushRRuleUtils.nextOccurrence(
            rrule = rrule,
            dtStart = dtstart,
            zoneId = zoneId,
            after = after
        ) ?: return null
        val scheduledAt = occurrence.minus(Duration.ofMinutes(leadTimeMinutes.toLong()))
        if (!scheduledAt.isAfter(now)) {
            return null
        }
        return scheduledAt
    }

}
