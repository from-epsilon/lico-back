package com.epsilon.nagginggnome.infra.persistence.jooq.llm

import com.epsilon.nagginggnome.domain.llm.repository.LlmJobRepository
import com.epsilon.nagginggnome.domain.llm.repository.model.LlmJobCreateModel
import com.epsilon.nagginggnome.generated.jooq.tables.references.LLM_JOBS
import org.jooq.DSLContext
import org.jooq.JSONB
import org.springframework.stereotype.Repository

/**
 * LlmJobRepository의 jOOQ 기반 구현체
 */
@Repository
class LlmJobRepositoryJooqAdapter(
    private val dsl: DSLContext
) : LlmJobRepository {

    /**
     * 작업을 대량으로 생성
     */
    override fun insertLlmJobs(models: List<LlmJobCreateModel>): Int {
        models.takeIf { it.isNotEmpty() } ?: return 0

        val chunkSize = 500

        return models.chunked(chunkSize).sumOf { chunk ->
            val records = chunk.map { model ->
                dsl.newRecord(LLM_JOBS).apply {
                    set(LLM_JOBS.TYPE, model.type.name)
                    set(LLM_JOBS.STATUS, model.status.name)
                    set(LLM_JOBS.INPUT_JSON, JSONB.valueOf(model.inputJson))
                }
            }
            dsl.batchInsert(records).execute().sum()
        }
    }
}
