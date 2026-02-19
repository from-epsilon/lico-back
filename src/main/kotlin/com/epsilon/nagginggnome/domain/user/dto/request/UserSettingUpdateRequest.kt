package com.epsilon.nagginggnome.domain.user.dto.request

import com.epsilon.nagginggnome.domain.user.constant.UserTier

/**
 * 유저 설정 정보 수정 요청 DTO
 */
data class UserSettingUpdateRequest(
    val nickname: String?,
    val verbosityPerDay: Double?,
    val sleepTime: Int?,
    val wakeTime: Int?,
    val timezone: String?,
    val tier: UserTier?
)
