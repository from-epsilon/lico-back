package com.epsilon.nagginggnome.infra.persistence.jooq.llm

import com.epsilon.nagginggnome.domain.llm.constant.LlmJobStatus
import com.epsilon.nagginggnome.domain.llm.constant.LlmJobType
import com.epsilon.nagginggnome.domain.llm.repository.LlmJobRepository
import com.epsilon.nagginggnome.domain.llm.repository.model.LlmJobApplyModel
import com.epsilon.nagginggnome.domain.llm.repository.model.LlmJobCreateModel
import com.epsilon.nagginggnome.generated.jooq.tables.references.LLM_JOBS
import org.jooq.DSLContext
import org.jooq.JSONB
import org.springframework.stereotype.Repository
import java.time.Instant

/**
 * LlmJobRepository의 jOOQ 기반 구현체
 */
@Repository
class LlmJobRepositoryJooqAdapter(
    private val dsl: DSLContext
) : LlmJobRepository {

    /**
     * 작업을 단건 생성
     */
    override fun insertLlmJob(model: LlmJobCreateModel): Int {
        return dsl.insertInto(LLM_JOBS)
            .set(LLM_JOBS.TYPE, model.type.name)
            .set(LLM_JOBS.STATUS, model.status.name)
            .set(LLM_JOBS.INPUT_JSON, JSONB.valueOf(model.inputJson))
            .execute()
    }

    /**
     * 적용 가능한 SUCCESS 작업을 락으로 조회
     */
    override fun lockNextSuccessUnapplied(limit: Int): List<LlmJobApplyModel> {
        if (limit <= 0) {
            return emptyList()
        }

        return dsl
            .selectFrom(LLM_JOBS)
            .where(
                LLM_JOBS.STATUS.eq(LlmJobStatus.SUCCESS.name)
                    .and(LLM_JOBS.APPLIED_AT.isNull)
                    .and(LLM_JOBS.OUTPUT_JSON.isNotNull)
            )
            .orderBy(LLM_JOBS.ID.asc())
            .limit(limit)
            .forUpdate()
            .skipLocked()
            .fetch { rec ->
                LlmJobApplyModel(
                    id = requireNotNull(rec.id),
                    type = LlmJobType.valueOf(requireNotNull(rec.type)),
                    inputJson = requireNotNull(rec.get(LLM_JOBS.INPUT_JSON)).data(),
                    outputJson = requireNotNull(rec.get(LLM_JOBS.OUTPUT_JSON)).data()
                )
            }
    }

    override fun lockSuccessUnappliedById(id: Long): LlmJobApplyModel? {
        return dsl
            .selectFrom(LLM_JOBS)
            .where(
                LLM_JOBS.ID.eq(id)
                    .and(LLM_JOBS.STATUS.eq(LlmJobStatus.SUCCESS.name))
                    .and(LLM_JOBS.APPLIED_AT.isNull)
                    .and(LLM_JOBS.OUTPUT_JSON.isNotNull)
            )
            .forUpdate()
            .skipLocked()
            .fetchOne { rec ->
                LlmJobApplyModel(
                    id = requireNotNull(rec.id),
                    type = LlmJobType.valueOf(requireNotNull(rec.type)),
                    inputJson = requireNotNull(rec.get(LLM_JOBS.INPUT_JSON)).data(),
                    outputJson = requireNotNull(rec.get(LLM_JOBS.OUTPUT_JSON)).data()
                )
            }
    }

    /**
     * 적용 완료 처리
     */
    override fun markApplied(ids: List<Long>, appliedAt: Instant): Int {
        ids.takeIf { it.isNotEmpty() } ?: return 0
        return dsl.update(LLM_JOBS)
            .set(LLM_JOBS.APPLIED_AT, appliedAt)
            .where(LLM_JOBS.ID.`in`(ids))
            .execute()
    }
}
