package com.epsilon.nagginggnome.infra.persistence.jpa.message

import com.epsilon.nagginggnome.domain.message.dto.response.ChatMessageListItemResponse
import com.epsilon.nagginggnome.domain.message.entity.ChatMessage
import com.epsilon.nagginggnome.domain.message.repository.ChatMessageRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
class ChatMessageRepositoryJpaAdapter(
    private val jpa: JpaChatMessageRepository
) : ChatMessageRepository {

    override fun save(entity: ChatMessage): ChatMessage {
        return jpa.save(entity)
    }

    override fun findChatMessagesByPlanId(
        planId: UUID,
        pageable: Pageable
    ): Page<ChatMessageListItemResponse> {
        return jpa.findChatMessagesByPlanId(planId, pageable)
    }
}
