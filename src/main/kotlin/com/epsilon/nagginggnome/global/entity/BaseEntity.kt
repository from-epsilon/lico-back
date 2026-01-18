package com.epsilon.nagginggnome.global.entity

import jakarta.persistence.Column
import jakarta.persistence.EntityListeners
import jakarta.persistence.MappedSuperclass
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.Instant

/**
 * 모든 엔티티가 공통으로 상속받는 기본 엔티티
 */
@MappedSuperclass
@EntityListeners(AuditingEntityListener::class)
abstract class BaseEntity {

    /**
     * 생성 시각
     */
    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    var createdAt: Instant? = null
        protected set

    /**
     * 수정 시각
     */
    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    var updatedAt: Instant? = null
        protected set

    /**
     * 삭제 시각
     * - null: 미삭제
     * - not null: 삭제
     */
    @Column(name = "deleted_at")
    var deletedAt: Instant? = null
        protected set

    /**
     * Entity 삭제
     */
    fun softDelete() {
        if (this.deletedAt != null) return  // 이미 삭제된 경우 멱등 처리
        this.deletedAt = Instant.now()
    }

    /**
     * Entity 복원
     */
    fun restore() {
        this.deletedAt = null
    }

    /**
     * 삭제 여부 확인
     */
    fun isDeleted(): Boolean = this.deletedAt != null
}
