package com.epsilon.nagginggnome.domain.message.repository

import com.epsilon.nagginggnome.domain.message.dto.response.ChatMessageListItemResponse
import com.epsilon.nagginggnome.domain.message.entity.ChatMessage
import io.lettuce.core.dynamic.annotation.Param
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface ChatMessageRepository : JpaRepository<ChatMessage, Long> {

    /**
     * 특정 플랜(planId)의 채팅 메시지를 최신순으로 조회
     */
    @Query(
        value = """
            SELECT new com.epsilon.nagginggnome.domain.message.dto.response.ChatMessageListItemResponse(
                cm.id,
                cm.snapshotId,
                cm.snapshotVersion,
                cm.type,
                cm.content,
                cm.createdAt
            )
            FROM ChatMessage cm
            WHERE cm.planId = :planId
            ORDER BY cm.createdAt desc
        """,
        countQuery = """
            SELECT count(cm.id)
            FROM ChatMessage cm
            WHERE cm.planId = :planId
        """
    )
    fun findChatMessagesByPlanId(
        @Param("planId") planId: UUID,
        pageable: Pageable
    ): Page<ChatMessageListItemResponse>
}
