package com.epsilon.nagginggnome.domain.message.service

import com.epsilon.nagginggnome.domain.message.constant.UserMessageType
import com.epsilon.nagginggnome.domain.message.dto.request.UserMessageCreateRequest
import com.epsilon.nagginggnome.domain.message.dto.request.UserMessageUpdateRequest
import com.epsilon.nagginggnome.domain.message.repository.ServerMessageRepository
import com.epsilon.nagginggnome.domain.message.repository.UserMessageRepository
import com.epsilon.nagginggnome.domain.message.repository.model.UserMessageCreateModel
import com.epsilon.nagginggnome.domain.plan.repository.PlanRepository
import com.epsilon.nagginggnome.domain.plan.repository.PlanSnapshotRepository
import com.epsilon.nagginggnome.global.constant.code.CommonErrorCode
import com.epsilon.nagginggnome.global.constant.code.MessageErrorCode
import com.epsilon.nagginggnome.global.constant.code.PlanErrorCode
import com.epsilon.nagginggnome.global.exception.ApiException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
class UserMessageService(
    private val planRepository: PlanRepository,
    private val planSnapshotRepository: PlanSnapshotRepository,
    private val userMessageRepository: UserMessageRepository,
    private val serverMessageRepository: ServerMessageRepository
) {

    @Transactional
    fun create(userId: UUID, planId: UUID, req: UserMessageCreateRequest) {
        if (req.planId != planId) {
            throw ApiException(CommonErrorCode.BAD_REQUEST)
        }

        val plan = planRepository.findByIdAndUserId(planId = planId, userId = userId)
            ?: throw ApiException(PlanErrorCode.PLAN_NOT_FOUND)

        if (plan.isDeleted()) {
            throw ApiException(PlanErrorCode.PLAN_NOT_FOUND)
        }

        if (userMessageRepository.existsByClientMessageId(req.id)) {
            throw ApiException(MessageErrorCode.DUPLICATE_MESSAGE_ID)
        }

        if (!serverMessageRepository.existsByIdAndUserIdAndPlanId(req.serverMessageId, userId, planId)) {
            throw ApiException(MessageErrorCode.SERVER_MESSAGE_NOT_FOUND)
        }

        val snapshot = planSnapshotRepository.findLatestByPlanId(planId)
            ?: throw ApiException(PlanErrorCode.PLAN_INVALID_STATE)

        val snapshotId = snapshot.id ?: throw ApiException(PlanErrorCode.PLAN_INVALID_STATE)

        userMessageRepository.insert(
            UserMessageCreateModel(
                planId = planId,
                snapshotId = snapshotId,
                snapshotVersion = snapshot.version,
                body = req.body,
                type = UserMessageType.USER_REPLY,
                clientMessageId = req.id,
                serverMessageId = req.serverMessageId,
                sentAt = req.sentAt
            )
        )
    }

    @Transactional
    fun update(userId: UUID, planId: UUID, messageId: UUID, req: UserMessageUpdateRequest) {
        val plan = planRepository.findByIdAndUserId(planId = planId, userId = userId)
            ?: throw ApiException(PlanErrorCode.PLAN_NOT_FOUND)

        if (plan.isDeleted()) {
            throw ApiException(PlanErrorCode.PLAN_NOT_FOUND)
        }

        val message = userMessageRepository.findByClientMessageIdAndPlanId(messageId, planId)
            ?: throw ApiException(MessageErrorCode.MESSAGE_NOT_FOUND)

        if (message.type != UserMessageType.USER_REPLY) {
            throw ApiException(CommonErrorCode.FORBIDDEN)
        }

        val updated = userMessageRepository.updateContent(messageId, planId, req.body, req.sentAt)
        if (updated == 0) {
            throw ApiException(MessageErrorCode.MESSAGE_NOT_FOUND)
        }
    }
}
