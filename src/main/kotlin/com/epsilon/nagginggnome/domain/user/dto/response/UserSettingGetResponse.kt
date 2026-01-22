package com.epsilon.nagginggnome.domain.user.dto.response

/**
 * 유저 설정 정보 조회 응답 DTO
 */
data class UserSettingGetResponse(
    val nickname: String,
    val verbosityPerDay: Double,
    val sleepTime: Int,
    val wakeTime: Int
)
