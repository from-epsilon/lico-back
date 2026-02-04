package com.epsilon.nagginggnome.domain.message.entity

import com.epsilon.nagginggnome.domain.message.constant.ChatMessageType
import com.epsilon.nagginggnome.global.entity.BaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.util.UUID

/**
 * chat_messages 테이블 매핑 엔티티
 */
@Entity
@Table(
    name = "chat_messages",
)
class ChatMessage(
    planId: UUID,
    snapshotId: UUID,
    snapshotVersion: Int,
    content: String,
    type: ChatMessageType
) : BaseEntity() {

    /**
     * 고유 ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    var id: Long? = null
        private set

    /**
     * 메시지가 발생된 플랜 ID
     */
    @Column(name = "plan_id", nullable = false, columnDefinition = "uuid")
    var planId: UUID = planId
        private set

    /**
     * 메시지가 발생된 스냅샷 ID
     */
    @Column(name = "snapshot_id", nullable = false, columnDefinition = "uuid")
    var snapshotId: UUID = snapshotId
        private set

    /**
     * 메시지가 발생된 스냅샷의 버전
     */
    @Column(name = "snapshot_version", nullable = false)
    var snapshotVersion: Int = snapshotVersion
        private set

    /**
     * 메시지 내용
     */
    @Column(name = "content", nullable = false, columnDefinition = "text")
    var content: String = content
        private set

    /**
     * 메시지 타입(NAGGING, PLAN_HISTORY, USER_REPLY)
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    var type: ChatMessageType = type
        private set
}
