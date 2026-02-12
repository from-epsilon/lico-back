package com.epsilon.nagginggnome.infra.persistence.jooq.push

import com.epsilon.nagginggnome.domain.message.constant.MessageType
import com.epsilon.nagginggnome.domain.push.constant.PushJobStatus
import com.epsilon.nagginggnome.domain.push.repository.PushJobRepository
import com.epsilon.nagginggnome.domain.push.repository.model.PushJobCreateModel
import com.epsilon.nagginggnome.domain.push.repository.model.PushJobProcessingModel
import com.epsilon.nagginggnome.generated.jooq.tables.references.PUSH_JOBS
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
     * 특정 플랜의 특정 시점 이후 READY 작업을 삭제
     * - scheduled_at >= from
     */
    override fun deleteReadyByPlanFrom(
        planId: UUID,
        from: Instant
    ): Int {
        return dsl
            .deleteFrom(PUSH_JOBS)
            .where(
                PUSH_JOBS.PLAN_ID.eq(planId)
                    .and(PUSH_JOBS.STATUS.eq(PushJobStatus.READY.name))
                    .and(PUSH_JOBS.SCHEDULED_AT.ge(from))
            )
            .execute()
    }

    /**
     * 작업을 대량으로 생성
     */
    override fun insertPushJob(
        userId: UUID,
        models: List<PushJobCreateModel>
    ): Int {
        models.takeIf { it.isNotEmpty() } ?: return 0

        val chunkSize = 500

        return models.chunked(chunkSize).sumOf { chunk ->
            val records = chunk.map { model ->
                dsl.newRecord(PUSH_JOBS).apply {
                    set(PUSH_JOBS.USER_ID, userId)
                    set(PUSH_JOBS.PLAN_ID, model.planId)
                    set(PUSH_JOBS.TYPE, model.type.name)
                    set(PUSH_JOBS.TITLE, model.title)
                    set(PUSH_JOBS.BODY, model.body)
                    set(PUSH_JOBS.DATA_JSON, model.dataJson?.let(JSONB::valueOf))
                    set(PUSH_JOBS.LLM_META_JSON, model.llmMetaJson?.let(JSONB::valueOf))
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
                    type = MessageType.valueOf(rec.type),
                    title = rec.title,
                    body = rec.body,
                    dataJson = dataJsonString,
                    llmMetaJson = rec.get(PUSH_JOBS.LLM_META_JSON)?.data(),
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
