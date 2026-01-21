package com.epsilon.nagginggnome.domain.push.dto.request

/**
 * FCM토큰 생성, 수정 요청 DTO
 */
data class FcmTokenUpsertRequest(
    val fcmToken: String
)
