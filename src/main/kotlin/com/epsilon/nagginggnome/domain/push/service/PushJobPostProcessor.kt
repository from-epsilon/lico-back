package com.epsilon.nagginggnome.domain.push.service

import com.epsilon.nagginggnome.domain.llm.service.LlmJobService
import com.epsilon.nagginggnome.domain.message.constant.MessageType
import com.epsilon.nagginggnome.domain.message.repository.ServerMessageRepository
import com.epsilon.nagginggnome.domain.message.repository.model.ServerMessageCreateModel
import com.epsilon.nagginggnome.domain.plan.constant.PlanStatus
import com.epsilon.nagginggnome.domain.plan.repository.PlanRepository
import com.epsilon.nagginggnome.domain.push.repository.model.PushJobProcessingModel
import com.epsilon.nagginggnome.domain.user.repository.UserSettingRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Instant

@Service
class PushJobPostProcessor(
    private val serverMessageRepository: ServerMessageRepository,
    private val planRepository: PlanRepository,
    private val userSettingRepository: UserSettingRepository,
    private val llmJobService: LlmJobService
) {

    @Transactional
    fun recordAndScheduleNext(job: PushJobProcessingModel) {
        serverMessageRepository.insert(
            ServerMessageCreateModel(
                userId = job.userId,
                planId = requireNotNull(job.planId),
                title = requireNotNull(job.title),
                body = requireNotNull(job.body),
                dataJson = job.dataJson,
                type = job.type,
                llmMetaJson = job.llmMetaJson
            )
        )

        if (job.type != MessageType.REMINDER) return

        val planId = requireNotNull(job.planId)
        val plan = planRepository.findByIdAndUserId(planId = planId, userId = job.userId) ?: return
        if (!plan.remind || plan.status != PlanStatus.ACTIVE) return
        val setting = userSettingRepository.findById(job.userId) ?: return
        llmJobService.enqueueReminderForPlan(
            now = Instant.now(),
            userId = job.userId,
            planId = plan.id,
            timezone = setting.timezone,
            rrule = plan.rrule,
            dtstart = plan.dtstart,
            leadTime = plan.leadTime
        )
    }
}
