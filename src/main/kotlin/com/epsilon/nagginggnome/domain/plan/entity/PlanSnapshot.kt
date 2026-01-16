package com.epsilon.nagginggnome.domain.plan.entity

import com.epsilon.nagginggnome.global.entity.BaseEntity
import jakarta.persistence.*
import java.time.Instant

/**
 * plan_snapshots 테이블 매핑 엔티티
 *
 * 역할
 * - 플랜 내용의 스냅샷 저장
 * - 버전 단조 증가(1씩 증가) 정책에 따른 이력 관리
 */
@Entity
@Table(
    name = "plan_snapshots",
    uniqueConstraints = [
        UniqueConstraint(
            name = "uk_plan_snapshots_plan_id_version",
            columnNames = ["plan_id", "version"]
        )
    ],
    indexes = [
        Index(name = "idx_plan_snapshots_plan_id_version", columnList = "plan_id, version")
    ]
)
@SequenceGenerator(
    name = "plan_snapshot_seq",
    sequenceName = "plan_snapshot_seq",
)
class PlanSnapshot(
    plan: Plan,
    version: Int,
    action: String,
    rrule: String,
    dtstart: Instant,
    purpose: String? = null,
    motive: String? = null,
    memo: String? = null
) : BaseEntity() {

    /**
     * 고유 ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "plan_snapshot_seq")
    @Column(name = "id", nullable = false, updatable = false)
    var id: Long? = null
        private set

    /**
     * 플랜
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "plan_id", nullable = false)
    var plan: Plan = plan
        private set

    /**
     * 플랜 버전
     */
    @Column(name = "version", nullable = false)
    var version: Int = version
        private set

    /**
     * 행동
     */
    @Column(name = "action", nullable = false)
    var action: String = action
        private set

    /**
     * 반복 규칙
     */
    @Column(name = "rrule", nullable = false)
    var rrule: String = rrule
        private set

    /**
     * 시작 시각
     */
    @Column(name = "dtstart", nullable = false)
    var dtstart: Instant = dtstart
        private set

    /**
     * 목적
     */
    @Column(name = "purpose")
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
}
