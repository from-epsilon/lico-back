package com.epsilon.nagginggnome.domain.user.dto.request

/**
 * 유저 설정 정보 생성 요청 DTO
 */
data class UserSettingCreateRequest(
    val nickname: String,
    val verbosityPerDay: Double,
    val sleepTime: Int,
    val wakeTime: Int,
    val timezone: String
)
