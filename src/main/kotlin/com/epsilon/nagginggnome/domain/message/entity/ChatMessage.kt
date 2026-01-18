package com.epsilon.nagginggnome.domain.message.entity

import com.epsilon.nagginggnome.domain.message.constant.ChatMessageType
import com.epsilon.nagginggnome.global.entity.BaseEntity
import jakarta.persistence.*

/**
 * chat_messages 테이블 매핑 엔티티
 */
@Entity
@Table(
    name = "chat_messages",
    indexes = [
        Index(name = "idx_chat_messages_plan_id_created_at", columnList = "plan_id, created_at")
    ]
)
@SequenceGenerator(
    name = "chat_message_seq",
    sequenceName = "chat_message_seq"
)
class ChatMessage(
    planId: Long,
    snapshotId: Long,
    version: Int,
    content: String,
    type: ChatMessageType
) : BaseEntity() {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "chat_message_seq")
    @Column(name = "id", nullable = false, updatable = false)
    var id: Long? = null
        private set

    @Column(name = "plan_id", nullable = false)
    var planId: Long = planId
        private set

    @Column(name = "snapshot_id", nullable = false)
    var snapshotId: Long = snapshotId
        private set

    @Column(name = "version", nullable = false)
    var version: Int = version
        private set

    @Column(name = "content", nullable = false, columnDefinition = "text")
    var content: String = content
        private set

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    var type: ChatMessageType = type
        private set
}
