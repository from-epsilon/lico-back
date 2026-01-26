package com.epsilon.nagginggnome.domain.plan.entity

import com.epsilon.nagginggnome.domain.plan.constant.PlanStatus
import com.epsilon.nagginggnome.domain.user.entity.User
import com.epsilon.nagginggnome.global.entity.BaseEntity
import jakarta.persistence.*
import java.time.Instant
import java.util.*

/**
 * plans 테이블 매핑 엔티티
 */
@Entity
@Table(name = "plans")
class Plan(
    planId: UUID,
    user: User,
    action: String,
    purpose: String? = null,
    motive: String? = null,
    memo: String? = null,
    dtstart: Instant,
    rrule: String,
    remind: Boolean = false,
    leadTime: Int? = null,
    currentVersion: Int = 0,
    currentSnapshotAt: Instant = Instant.now(),
    status: PlanStatus = PlanStatus.ACTIVE,
) : BaseEntity() {

    /**
     * 고유 ID
     */
    @Id
    @Column(name = "id", nullable = false, updatable = false, columnDefinition = "uuid")
    var id: UUID = planId
        private set

    /**
     * 사용자
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    var user: User = user
        private set

    /**
     * 행동(Title)
     */
    @Column(name = "action", nullable = false, columnDefinition = "text")
    var action: String = action
        private set

    /**
     * 목적
     */
    @Column(name = "purpose", columnDefinition = "text")
    var purpose: String? = purpose
        private set

    /**
     * 동기
     */
    @Column(name = "motive", columnDefinition = "text")
    var motive: String? = motive
        private set

    /**
     * 메모
     */
    @Column(name = "memo", columnDefinition = "text")
    var memo: String? = memo
        private set

    /**
     * 시작 일시
     */
    @Column(name = "dtstart", nullable = false)
    var dtstart: Instant = dtstart
        private set

    /**
     * 반복 규칙(RRULE)
     */
    @Column(name = "rrule", nullable = false, columnDefinition = "text")
    var rrule: String = rrule
        private set

    /**
     * 알림 여부
     */
    @Column(name = "remind", nullable = false)
    var remind: Boolean = remind
        private set

    /**
     * 준비 시간(분 단위)
     */
    @Column(name = "lead_time")
    var leadTime: Int? = leadTime
        private set

    /**
     * 최근 스냅샷 버전
     */
    @Column(name = "current_version", nullable = false)
    var currentVersion: Int = currentVersion
        private set

    /**
     * 최근 스냅샷 시점
     */
    @Column(name = "current_snapshot_at", nullable = false)
    var currentSnapshotAt: Instant = currentSnapshotAt
        private set

    /**
     * 상태
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    var status: PlanStatus = status
        private set
}
