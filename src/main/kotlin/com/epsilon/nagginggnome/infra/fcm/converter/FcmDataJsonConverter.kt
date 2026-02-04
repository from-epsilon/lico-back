package com.epsilon.nagginggnome.infra.fcm.converter

import tools.jackson.core.type.TypeReference
import tools.jackson.databind.ObjectMapper

/**
 * FCM data payload 생성을 위한 JSON 변환 유틸리티
 */
object FcmDataJsonConverter {
    private val fcmDataTypeRef = object : TypeReference<Map<String, String>>() {}

    fun toFcmDataMap(objectMapper: ObjectMapper, data: String?): Map<String, String> {
        if (data.isNullOrBlank()) {
            return emptyMap()
        }
        return objectMapper.readValue(data, fcmDataTypeRef)
    }
}
