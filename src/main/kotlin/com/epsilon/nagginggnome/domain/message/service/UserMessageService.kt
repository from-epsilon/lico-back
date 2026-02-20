package com.epsilon.nagginggnome.domain.message.service

import com.epsilon.nagginggnome.domain.llm.constant.LlmJobTargetType
import com.epsilon.nagginggnome.domain.message.dto.request.UserMessageCreateRequest
import com.epsilon.nagginggnome.domain.message.dto.request.UserMessageUpdateRequest
import com.epsilon.nagginggnome.domain.message.repository.ServerMessageRepository
import com.epsilon.nagginggnome.domain.message.repository.UserMessageRepository
import com.epsilon.nagginggnome.domain.message.repository.model.UserMessageCreateModel
import com.epsilon.nagginggnome.domain.plan.repository.PlanLogRepository
import com.epsilon.nagginggnome.domain.plan.repository.PlanRepository
import com.epsilon.nagginggnome.global.constant.code.CommonErrorCode
import com.epsilon.nagginggnome.global.constant.code.MessageErrorCode
import com.epsilon.nagginggnome.global.constant.code.PlanErrorCode
import com.epsilon.nagginggnome.global.exception.ApiException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import tools.jackson.databind.ObjectMapper
import java.util.UUID

@Service
class UserMessageService(
    private val planRepository: PlanRepository,
    private val userMessageRepository: UserMessageRepository,
    private val serverMessageRepository: ServerMessageRepository,
    private val planLogRepository: PlanLogRepository,
    private val objectMapper: ObjectMapper
) {

    @Transactional
    fun create(userId: UUID, planId: UUID, req: UserMessageCreateRequest) {
        if (req.planId != planId) {
            throw ApiException(
                errorCode = CommonErrorCode.BAD_REQUEST
            )
        }

        val plan = planRepository.findByIdAndUserId(planId = planId, userId = userId)
            ?: throw ApiException(
                errorCode = PlanErrorCode.PLAN_NOT_FOUND
            )

        if (plan.isDeleted()) {
            throw ApiException(
                errorCode = PlanErrorCode.PLAN_NOT_FOUND
            )
        }

        if (userMessageRepository.existsById(id = req.id)) {
            throw ApiException(
                errorCode = MessageErrorCode.DUPLICATE_MESSAGE_ID
            )
        }

        val exists = serverMessageRepository.existsByIdAndUserIdAndPlanId(
            id = req.serverMessageId,
            userId = userId,
            planId = planId
        )
        if (!exists) {
            throw ApiException(
                errorCode = MessageErrorCode.SERVER_MESSAGE_NOT_FOUND
            )
        }

        userMessageRepository.insert(
            model = UserMessageCreateModel(
                id = req.id,
                userId = userId,
                planId = planId,
                body = req.body,
                serverMessageId = req.serverMessageId,
                sentAt = req.sentAt
            )
        )

        serverMessageRepository.findByIdAndUserIdAndPlanId(
            id = req.serverMessageId,
            userId = userId,
            planId = planId
        )?.let { serverMessage ->
            val log = mapOf(
                "type" to LlmJobTargetType.USER_MESSAGE.name,
                "timestamp" to req.sentAt,
                "content" to mapOf(
                    "reply_to" to mapOf(
                        "title" to serverMessage.title,
                        "body" to serverMessage.body
                    ),
                    "body" to req.body
                )
            )
            planLogRepository.appendRecentLog(
                planId = planId,
                logJson = objectMapper.writeValueAsString(log)
            )
        }
    }

    @Transactional
    fun update(userId: UUID, planId: UUID, messageId: UUID, req: UserMessageUpdateRequest) {
        val plan = planRepository.findByIdAndUserId(planId = planId, userId = userId)
            ?: throw ApiException(
                errorCode = PlanErrorCode.PLAN_NOT_FOUND
            )

        if (plan.isDeleted()) {
            throw ApiException(
                errorCode = PlanErrorCode.PLAN_NOT_FOUND
            )
        }

        val exists = userMessageRepository.existsByIdAndPlanId(
            id = messageId,
            planId = planId
        )
        if (!exists) {
            throw ApiException(
                errorCode = MessageErrorCode.MESSAGE_NOT_FOUND
            )
        }

        val updated = userMessageRepository.updateContent(
            id = messageId,
            planId = planId,
            body = req.body,
            sentAt = req.sentAt
        )
        if (updated == 0) {
            throw ApiException(
                errorCode = MessageErrorCode.MESSAGE_NOT_FOUND
            )
        }
    }
}
