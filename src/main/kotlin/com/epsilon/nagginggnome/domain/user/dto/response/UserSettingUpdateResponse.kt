package com.epsilon.nagginggnome.domain.user.dto.response

import com.epsilon.nagginggnome.domain.user.constant.UserTier

/**
 * 유저 설정 정보 수정 응답 DTO
 */
data class UserSettingUpdateResponse(
    val nickname: String,
    val verbosityPerDay: Double,
    val sleepTime: Int,
    val wakeTime: Int,
    val timezone: String,
    val tier: UserTier
)
