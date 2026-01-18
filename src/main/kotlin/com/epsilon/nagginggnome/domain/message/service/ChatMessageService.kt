package com.epsilon.nagginggnome.domain.message.service

import com.epsilon.nagginggnome.domain.message.constant.ChatMessageType
import com.epsilon.nagginggnome.domain.message.entity.ChatMessage
import com.epsilon.nagginggnome.domain.message.repository.ChatMessageRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ChatMessageService(
    private val chatMessageRepository: ChatMessageRepository
) {
    @Transactional
    fun appendPlanHistoryMessage(planId: Long, snapshotId: Long, version: Int, content: String) {
        chatMessageRepository.save(
            ChatMessage(
                planId = planId,
                snapshotId = snapshotId,
                version = version,
                content = content,
                type = ChatMessageType.PLAN_HISTORY
            )
        )
    }
}
