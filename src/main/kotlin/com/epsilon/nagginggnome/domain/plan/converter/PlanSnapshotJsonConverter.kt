package com.epsilon.nagginggnome.domain.plan.converter

import tools.jackson.core.type.TypeReference
import tools.jackson.databind.ObjectMapper

/**
 * 스냅샷 jsonb 저장을 위한 JSON 변환 유틸리티
 */
object PlanSnapshotJsonConverter {
    private val snapshotDataTypeRef = object : TypeReference<Map<String, Any?>>() {}

    fun toJsonMap(objectMapper: ObjectMapper, snapshotData: Any): Map<String, Any?> {
        return objectMapper.convertValue(snapshotData, snapshotDataTypeRef)
    }
}
