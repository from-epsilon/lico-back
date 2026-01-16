package com.epsilon.nagginggnome.domain.plan.entity

import com.epsilon.nagginggnome.domain.user.entity.User
import com.epsilon.nagginggnome.global.entity.BaseEntity
import jakarta.persistence.*

/**
 * plans 테이블 매핑 엔티티
 * 
 * 역할
 * - 최신 스냅샷 포인터(currentSnapshotId) 및 최신 버전(currentVersion) 캐시 유지
 */
@Entity
@Table(
    name = "plans",
    indexes = [
        Index(name = "idx_plans_user_id", columnList = "user_id")
    ]
)
@SequenceGenerator(
    name = "plan_seq",
    sequenceName = "plan_seq"
)
class Plan(
    user: User,
    currentSnapshot: Long? = null,
    currentVersion: Int = 0
) : BaseEntity() {

    /**
     * 고유 ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "plan_seq")
    @Column(name = "id", nullable = false, updatable = false)
    var id: Long? = null
        private set

    /**
     * 사용자
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    var user: User = user
        private set

    /**
     * 현재 스냅샷
     */
    @Column(name = "current_snapshot_id")
    var currentSnapshotId: Long? = currentSnapshot
        private set

    /**
     * 현재 버전
     */
    @Column(name = "current_version", nullable = false)
    var currentVersion: Int = currentVersion
        private set

    /**
     * 스냅샷 생성 후 Plan에 현재 포인터를 갱신
     * - 트랜잭션 내에서 Snapshot INSERT 이후 호출
     */
    fun pointToSnapshot(snapshotId: Long, version: Int) {
        this.currentSnapshotId = snapshotId
        this.currentVersion = version
    }
}
