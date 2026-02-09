package com.epsilon.nagginggnome.domain.push.dto.request

/**
 * 공개 테스트용 FCM 토큰 입력 DTO
 */
data class PublicFcmTokenRequest(
    val fcmToken: String,
    val title: String? = null,
    val body: String? = null,
    val data: Map<String, String> = emptyMap()
)
