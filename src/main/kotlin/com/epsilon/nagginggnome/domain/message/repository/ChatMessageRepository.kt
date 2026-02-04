package com.epsilon.nagginggnome.domain.message.repository

import com.epsilon.nagginggnome.domain.message.dto.response.ChatMessageListItemResponse
import com.epsilon.nagginggnome.domain.message.entity.ChatMessage
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.util.UUID

interface ChatMessageRepository {
    fun save(entity: ChatMessage): ChatMessage

    fun findChatMessagesByPlanId(
        planId: UUID,
        pageable: Pageable
    ): Page<ChatMessageListItemResponse>
}
