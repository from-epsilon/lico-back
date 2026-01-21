package com.epsilon.nagginggnome.domain.user.dto.request

/**
 * 유저 상세 정보 생성 요청 DTO
 */
data class UserDetailCreateRequest(
    val nickname: String,
    val verbosityPerDay: Double,
    val sleepTime: Int,
    val wakeTime: Int
)
