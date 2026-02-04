package com.epsilon.nagginggnome.domain.plan.entity

import com.epsilon.nagginggnome.global.entity.BaseEntity
import jakarta.persistence.*
import org.hibernate.annotations.JdbcTypeCode
import org.hibernate.annotations.UuidGenerator
import org.hibernate.type.SqlTypes
import tools.jackson.databind.JsonNode
import java.time.Instant
import java.util.*

/**
 * plan_snapshots 테이블 매핑 엔티티
 */
@Entity
@Table(
    name = "plan_snapshots",
    uniqueConstraints = [
        UniqueConstraint(
            name = "uk_plan_snapshots_plan_id_version",
            columnNames = ["plan_id", "version"]
        )
    ]
)
class PlanSnapshot(
    planId: UUID,
    type: String,
    version: Int,
    dataJson: Map<String, Any?>,
    snapshotAt: Instant = Instant.now(),
) : BaseEntity() {

    /**
     * 고유 ID
     */
    @Id
    @UuidGenerator
    @Column(name = "id", nullable = false, updatable = false, columnDefinition = "uuid")
    var id: UUID? = null
        private set

    /**
     * 플랜 ID
     */
    @Column(name = "plan_id", nullable = false, columnDefinition = "uuid")
    var planId: UUID = planId
        private set

    /**
     * 스냅샷 타입
     */
    @Column(name = "type", nullable = false)
    var type: String = type
        private set

    /**
     * 스냅샷 버전
     */
    @Column(name = "version", nullable = false)
    var version: Int = version
        private set

    /**
     * 변경 직후의 플랜 전체 데이터(JSONB)
     */
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "data_json", nullable = false, columnDefinition = "jsonb")
    var dataJson: Map<String, Any?> = dataJson
        private set

    /**
     * 스냅샷 생성 시점
     */
    @Column(name = "snapshot_at", nullable = false)
    var snapshotAt: Instant = snapshotAt
        private set
}
