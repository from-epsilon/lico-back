package com.epsilon.nagginggnome.global.exception

import com.epsilon.nagginggnome.global.constant.code.CommonErrorCode
import com.epsilon.nagginggnome.global.constant.message.ApiMessages
import com.epsilon.nagginggnome.global.dto.response.ApiResponse
import com.epsilon.nagginggnome.global.logging.logger
import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import tools.jackson.databind.exc.InvalidFormatException

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

    /**
     * 요청 바디(JSON) 역직렬화 단계에서 발생하는 예외를 처리
     */
    @ExceptionHandler(HttpMessageNotReadableException::class)
    fun handleHttpMessageNotReadable(
        ex: HttpMessageNotReadableException,
        req: HttpServletRequest
    ): ResponseEntity<ApiResponse<Nothing>> {

        // rootCause를 분석하여 가능한 범위에서 "어떤 필드가 문제인지"만 추출
        // 역직렬화 단계에서는 보통 1개 필드에서 먼저 실패하는 구조
        val message = when (val root = ex.rootCause) {
            is InvalidFormatException -> {
                val property = root.path.lastOrNull()?.propertyName ?: "body"
                ApiMessages.invalidField(property)
            }

            else -> ApiMessages.INVALID_JSON_BODY
        }

        log.warn(
            "BadRequest method={}, uri={}, message={}",
            req.method,
            req.requestURI,
            message
        )

        val body = ApiResponse.fail(
            errorCode = CommonErrorCode.BAD_REQUEST,
            message = message
        )

        return ResponseEntity.status(CommonErrorCode.BAD_REQUEST.status).body(body)
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
