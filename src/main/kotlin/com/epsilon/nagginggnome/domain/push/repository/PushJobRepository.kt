package com.epsilon.nagginggnome.domain.push.repository

import com.epsilon.nagginggnome.domain.push.constant.PushJobStatus
import com.epsilon.nagginggnome.domain.push.repository.model.PushJobCreateModel
import com.epsilon.nagginggnome.domain.push.repository.model.PushJobProcessingModel
import com.epsilon.nagginggnome.domain.push.repository.model.PushJobRangeModel
import java.time.Instant
import java.util.*

/**
 * Push Job 저장소 인터페이스
 */
interface PushJobRepository {

    /**
     * 특정 유저의 특정 시간 범위에 있는 READY 작업을 삭제
     * - range 덮어쓰기(upsert) 시 기존 스케줄 제거
     */
    fun deleteReadyByUserAndRange(userId: UUID, range: PushJobRangeModel): Int

    /**
     * 작업을 대량으로 생성
     */
    fun insertBatch(models: List<PushJobCreateModel>): Int

    /**
     * 처리 가능한 READY 작업을 락으로 조회
     * - scheduled_at <= now
     * - status = READY
     * - ORDER BY scheduled_at
     * - FOR UPDATE SKIP LOCKED
     */
    fun lockNextReadyJobs(now: Instant, limit: Int): List<PushJobProcessingModel>

    /**
     * 작업 상태를 일괄 변경
     * - lock으로 가져온 후 RUNNING으로 전환
     */
    fun updateStatus(ids: List<Long>, status: PushJobStatus): Int

    /**
     * 단건 상태 변경
     * - 처리 완료(DONE), 처리 실패(FAILED)
     */
    fun updateStatus(id: Long, status: PushJobStatus): Int
}
