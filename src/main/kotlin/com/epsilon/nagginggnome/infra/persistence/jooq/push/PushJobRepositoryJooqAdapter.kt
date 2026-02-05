package com.epsilon.nagginggnome.infra.persistence.jooq.push

import com.epsilon.nagginggnome.domain.push.constant.PushJobStatus
import com.epsilon.nagginggnome.domain.push.constant.PushType
import com.epsilon.nagginggnome.domain.push.repository.PushJobRepository
import com.epsilon.nagginggnome.domain.push.repository.model.PushJobCreateModel
import com.epsilon.nagginggnome.domain.push.repository.model.PushJobProcessingModel
import com.epsilon.nagginggnome.generated.jooq.tables.references.PUSH_JOBS
import com.epsilon.nagginggnome.generated.jooq.tables.references.PUSH_JOB_BATCHES
import org.jooq.DSLContext
import org.jooq.JSONB
import org.springframework.stereotype.Repository
import java.time.Instant
import java.util.UUID

/**
 * PushJobRepository의 jOOQ 기반 구현체
 */
@Repository
class PushJobRepositoryJooqAdapter(
    private val dsl: DSLContext
) : PushJobRepository {

    /**
     * 배치 키를 멱등하게 삽입
     * - (user_id, batch_id)가 최초면 1 row 삽입, true 반환
     * - 이미 존재하면 do nothing, 0 row, false 반환
     */
    override fun tryInsertBatchKey(userId: UUID, batchId: UUID): Boolean {
        val insertedRows: Int =
            dsl.insertInto(PUSH_JOB_BATCHES)
                .set(PUSH_JOB_BATCHES.USER_ID, userId)
                .set(PUSH_JOB_BATCHES.BATCH_ID, batchId)
                .onConflict(PUSH_JOB_BATCHES.USER_ID, PUSH_JOB_BATCHES.BATCH_ID)
                .doNothing()
                .execute()

        return insertedRows == 1
    }

    /**
     * 특정 유저의 특정 시간 범위에 있는 READY 작업을 삭제
     * - [from, to]
     */
    override fun deletePushJobByUserAndRange(
        userId: UUID,
        from: Instant,
        to: Instant
    ): Int {
        return dsl
            .deleteFrom(PUSH_JOBS)
            .where(
                PUSH_JOBS.USER_ID.eq(userId)
                    .and(PUSH_JOBS.STATUS.eq(PushJobStatus.READY.name))
                    .and(PUSH_JOBS.SCHEDULED_AT.ge(from))
                    .and(PUSH_JOBS.SCHEDULED_AT.le(to))
            )
            .execute()
    }

    /**
     * 작업을 대량으로 생성
     */
    override fun insertPushJob(
        userId: UUID,
        batchId: UUID,
        models: List<PushJobCreateModel>
    ): Int {
        models.takeIf { it.isNotEmpty() } ?: return 0

        val chunkSize = 500

        return models.chunked(chunkSize).sumOf { chunk ->
            val records = chunk.map { model ->
                dsl.newRecord(PUSH_JOBS).apply {
                    set(PUSH_JOBS.USER_ID, userId)
                    set(PUSH_JOBS.BATCH_ID, batchId)
                    set(PUSH_JOBS.PLAN_ID, model.planId)
                    set(PUSH_JOBS.TYPE, model.type.name)
                    set(PUSH_JOBS.TITLE, model.title)
                    set(PUSH_JOBS.BODY, model.body)
                    set(PUSH_JOBS.DATA_JSON, model.dataJson?.let(JSONB::valueOf))
                    set(PUSH_JOBS.STATUS, model.status.name)
                    set(PUSH_JOBS.SCHEDULED_AT, model.scheduledAt)
                }
            }
            dsl.batchInsert(records).execute().sum()
        }
    }

    /**
     * 처리 가능한 READY 작업을 락으로 조회
     */
    override fun lockNextReadyJobs(
        now: Instant,
        limit: Int
    ): List<PushJobProcessingModel> {
        if (limit <= 0) {
            return emptyList()
        }
        return dsl
            .selectFrom(PUSH_JOBS)
            .where(
                PUSH_JOBS.STATUS.eq(PushJobStatus.READY.name)
                    .and(PUSH_JOBS.SCHEDULED_AT.le(now))
            )
            .orderBy(PUSH_JOBS.SCHEDULED_AT.asc(), PUSH_JOBS.ID.asc())
            .limit(limit)
            .forUpdate()
            .skipLocked()
            .fetch { rec ->
                val dataJsonString: String? = rec.get(PUSH_JOBS.DATA_JSON)?.data()
                PushJobProcessingModel(
                    id = requireNotNull(rec.id),
                    userId = rec.userId,
                    planId = rec.planId,
                    type = PushType.valueOf(rec.type),
                    title = rec.title,
                    body = rec.body,
                    dataJson = dataJsonString,
                    scheduledAt = rec.scheduledAt,
                    status = PushJobStatus.valueOf(requireNotNull(rec.status))
                )
            }
    }

    /**
     * 작업 상태를 일괄 변경
     */
    override fun updateStatus(
        ids: List<Long>,
        status: PushJobStatus
    ): Int {
        ids.takeIf { it.isNotEmpty() } ?: return 0
        return dsl.update(PUSH_JOBS)
            .set(PUSH_JOBS.STATUS, status.name)
            .where(PUSH_JOBS.ID.`in`(ids))
            .execute()
    }

    /**
     * 단건 상태 변경
     */
    override fun updateStatus(
        id: Long,
        status: PushJobStatus
    ): Int {
        return dsl.update(PUSH_JOBS)
            .set(PUSH_JOBS.STATUS, status.name)
            .where(PUSH_JOBS.ID.eq(id))
            .execute()
    }
}
