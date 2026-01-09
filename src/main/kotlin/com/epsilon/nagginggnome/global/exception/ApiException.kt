package com.epsilon.nagginggnome.global.exception

import com.epsilon.nagginggnome.global.constant.code.ErrorCode

/**
 * 애플리케이션 표준 예외
 */
class ApiException(
    val errorCode: ErrorCode,
    detailMessage: String? = null
) : RuntimeException(detailMessage ?: errorCode.message)
