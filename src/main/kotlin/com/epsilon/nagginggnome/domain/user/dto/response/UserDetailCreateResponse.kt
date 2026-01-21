package com.epsilon.nagginggnome.domain.user.dto.response

/**
 * 유저 상세 정보 생성 응답 DTO
 */
data class UserDetailCreateResponse(
    val nickname: String?,
    val verbosityPerDay: Double?,
    val sleepTime: Int?,
    val wakeTime: Int?
)
