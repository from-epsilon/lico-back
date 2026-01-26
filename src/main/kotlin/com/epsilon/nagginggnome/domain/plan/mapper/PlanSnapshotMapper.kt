package com.epsilon.nagginggnome.domain.plan.mapper

import com.epsilon.nagginggnome.domain.plan.dto.internal.PlanSnapshotData
import com.epsilon.nagginggnome.domain.plan.entity.Plan

/**
 * Plan, PlanSnapshotData 변환 매퍼
 */
object PlanSnapshotMapper {

    /**
     * Plan 엔티티를 스냅샷 DTO로 변환
     */
    fun toSnapshotData(plan: Plan): PlanSnapshotData =
        PlanSnapshotData(
            action = plan.action,
            purpose = plan.purpose,
            motive = plan.motive,
            memo = plan.memo,
            dtstart = plan.dtstart,
            rrule = plan.rrule,
            remind = plan.remind,
            leadTime = plan.leadTime,
            currentVersion = plan.currentVersion,
            currentSnapshotAt = plan.currentSnapshotAt,
            status = plan.status,
        )
}
