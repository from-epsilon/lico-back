package com.epsilon.nagginggnome.global.exception

import com.epsilon.nagginggnome.global.constant.code.CommonErrorCode
import com.epsilon.nagginggnome.global.dto.response.ApiResponse
import jakarta.servlet.http.HttpServletRequest
import org.springframework.boot.webmvc.error.ErrorController
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * Spring Boot 기본 /error(JSON: timestamp, status, error...) 응답을
 * 프로젝트 표준(ApiResponse)로 통일하기 위한 컨트롤러
 */
@RestController
@RequestMapping("/v1")
class ApiErrorController: ErrorController {

    @RequestMapping("/error")
    fun handleError(request: HttpServletRequest): ResponseEntity<ApiResponse<Nothing>> {

        /**
         * 서블릿 표준 에러 status_code 속성
         * 없으면 500으로 간주
         */
        val statusCode = (request.getAttribute("jakarta.servlet.error.status_code") as? Int)
            ?: HttpStatus.INTERNAL_SERVER_ERROR.value()

        /**
         * 프로젝트 에러코드로 매핑
         */
        val errorCode = when (statusCode) {
            HttpStatus.UNAUTHORIZED.value() -> CommonErrorCode.UNAUTHORIZED
            HttpStatus.FORBIDDEN.value() -> CommonErrorCode.FORBIDDEN
            HttpStatus.NOT_FOUND.value() -> CommonErrorCode.NOT_FOUND
            else -> CommonErrorCode.INTERNAL_SERVER_ERROR
        }

        return ResponseEntity.status(statusCode).body(ApiResponse.fail(errorCode))
    }
}
