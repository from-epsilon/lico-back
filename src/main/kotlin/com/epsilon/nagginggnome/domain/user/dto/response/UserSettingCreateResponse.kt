package com.epsilon.nagginggnome.domain.user.dto.response

import com.epsilon.nagginggnome.domain.user.constant.UserTier

/**
 * 유저 설정 정보 생성 응답 DTO
 */
data class UserSettingCreateResponse(
    val nickname: String,
    val verbosityPerDay: Double,
    val sleepTime: Int,
    val wakeTime: Int,
    val timezone: String,
    val tier: UserTier,
)
