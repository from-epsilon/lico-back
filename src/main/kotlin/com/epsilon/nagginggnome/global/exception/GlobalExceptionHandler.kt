package com.epsilon.nagginggnome.global.exception

import com.epsilon.nagginggnome.global.code.CommonErrorCode
import com.epsilon.nagginggnome.global.dto.response.ApiResponse
import com.epsilon.nagginggnome.global.logging.logger
import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

/**
 * 전역 예외 처리기
 */
@RestControllerAdvice
class GlobalExceptionHandler {

    companion object {
        private val log = logger<GlobalExceptionHandler>()
    }

    /**
     * 표준 예외(ApiException)를 표준 실패 응답(ApiResponse.fail)으로 변환
     */
    @ExceptionHandler(ApiException::class)
    fun handleApiException(
        ex: ApiException,
        req: HttpServletRequest
    ): ResponseEntity<ApiResponse<Nothing>> {
        log.warn(
            "ApiException method={}, uri={}, status={}, code={}, message={}",
            req.method,
            req.requestURI,
            ex.errorCode.status.value(),
            ex.errorCode.code,
            ex.message
        )

        val body = ApiResponse.fail(
            errorCode = ex.errorCode,
            message = ex.message ?: ex.errorCode.message
        )

        return ResponseEntity.status(ex.errorCode.status).body(body)
    }

    @ExceptionHandler(Exception::class)
    fun handleException(
        ex: Exception,
        req: HttpServletRequest
    ): ResponseEntity<ApiResponse<Nothing>> {
        log.error(
            "Exception method={}, uri={}",
            req.method,
            req.requestURI,
            ex
        )

        val body = ApiResponse.fail(
            errorCode = CommonErrorCode.INTERNAL_ERROR,
            message = CommonErrorCode.INTERNAL_ERROR.message
        )

        return ResponseEntity.status(CommonErrorCode.INTERNAL_ERROR.status).body(body)
    }
}
