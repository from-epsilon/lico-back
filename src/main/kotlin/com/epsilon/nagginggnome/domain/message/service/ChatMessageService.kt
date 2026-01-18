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
}
