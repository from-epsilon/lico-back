package com.epsilon.nagginggnome.domain.plan.repository

import com.epsilon.nagginggnome.domain.plan.dto.response.PlanListItemResponse
import com.epsilon.nagginggnome.domain.plan.entity.Plan
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface PlanRepository : JpaRepository<Plan, Long> {

    /**
     * 내 플랜 목록 조회
     * - 최신 스냅샷(currentSnapshotId)만 조인하여 목록 화면용 DTO로 반환
     */
    @Query(
        value = """
            SELECT new com.epsilon.nagginggnome.domain.plan.dto.response.PlanListItemResponse(
                    p.id,
                    p.currentVersion,
                    ps.id,
                    ps.action,
                    ps.rrule,
                    ps.dtstart
                    )
            FROM Plan p
            JOIN PlanSnapshot ps
                ON ps.id = p.currentSnapshotId
            WHERE p.user.id = :userId
                AND p.currentSnapshotId is not null
            ORDER BY p.updatedAt desc
        """,
        countQuery = """
            SELECT count(p.id)
            FROM Plan p
            WHERE p.user.id = :userId
              AND p.currentSnapshotId is not null
        """
    )
    fun findMyPlans(
        @Param("userId") userId: UUID,
        pageable: Pageable
    ): Page<PlanListItemResponse>
}
