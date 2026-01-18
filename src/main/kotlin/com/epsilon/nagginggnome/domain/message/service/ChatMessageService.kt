package com.epsilon.nagginggnome.domain.message.service

import com.epsilon.nagginggnome.domain.message.constant.ChatMessageType
import com.epsilon.nagginggnome.domain.message.dto.response.ChatMessageListItemResponse
import com.epsilon.nagginggnome.domain.message.entity.ChatMessage
import com.epsilon.nagginggnome.domain.message.repository.ChatMessageRepository
import com.epsilon.nagginggnome.domain.plan.repository.PlanRepository
import com.epsilon.nagginggnome.global.constant.code.PlanErrorCode
import com.epsilon.nagginggnome.global.exception.ApiException
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class ChatMessageService(
    private val planRepository: PlanRepository,
    private val chatMessageRepository: ChatMessageRepository
) {

    /**
     * 메시지 작성
     */
    @Transactional
    fun appendMessage(planId: Long, snapshotId: Long, snapshotVersion: Int, content: String, type: ChatMessageType) {
        chatMessageRepository.save(
            ChatMessage(
                planId = planId,
                snapshotId = snapshotId,
                snapshotVersion = snapshotVersion,
                content = content,
                type = type
            )
        )
    }

    @Transactional(readOnly = true)
    fun getPlanMessages(userId: UUID, planId: Long, pageable: Pageable): Page<ChatMessageListItemResponse> {
        // 소유권 검증
        val plan = planRepository.findByIdAndUserId(planId = planId, userId = userId)
            ?: throw ApiException(PlanErrorCode.PLAN_NOT_FOUND)

        // 삭제된 플랜은 접근 불가 처리
        if (plan.isDeleted()) {
            throw ApiException(PlanErrorCode.PLAN_NOT_FOUND)
        }

        return chatMessageRepository.findMessagesByPlanId(
            planId = planId,
            pageable = pageable
        )
    }
}
