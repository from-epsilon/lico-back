package com.epsilon.nagginggnome.domain.plan.repository

import com.epsilon.nagginggnome.domain.plan.entity.PlanSnapshot
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface PlanSnapshotRepository : JpaRepository<PlanSnapshot, Long> {

    /**
     * 특정 플랜의 스냅샷 버전 목록 조회
     */
    @Query(
        value = """
            SELECT ps.version
            FROM PlanSnapshot ps
            JOIN ps.plan p
            WHERE p.id = :planId
                AND p.user.id = :userId
                AND p.deletedAt is null
            ORDER BY ps.version desc
        """
    )
    fun findSnapShotVersionsByPlan(
        @Param("userId") userId: UUID,
        @Param("planId") planId: Long
    ): List<Int>
}
