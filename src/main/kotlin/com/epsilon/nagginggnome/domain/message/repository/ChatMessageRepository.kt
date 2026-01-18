package com.epsilon.nagginggnome.domain.message.repository

import com.epsilon.nagginggnome.domain.message.entity.ChatMessage
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ChatMessageRepository : JpaRepository<ChatMessage, Long>
