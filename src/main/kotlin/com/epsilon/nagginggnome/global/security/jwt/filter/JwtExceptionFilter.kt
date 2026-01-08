package com.epsilon.nagginggnome.global.security.jwt.filter

import com.epsilon.nagginggnome.global.dto.response.ApiResponse
import com.epsilon.nagginggnome.global.exception.ApiException
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import tools.jackson.databind.ObjectMapper
import java.nio.charset.StandardCharsets

/**
 * 필터 체인 실행 중 ApiException이 발생하면,
 * 표준 JSON 실패 응답(ApiResponse.fail)으로 변환하여 내려주는 필터
 */
@Component
class JwtExceptionFilter(
    private val objectMapper: ObjectMapper
) : OncePerRequestFilter() {
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        try {
            filterChain.doFilter(request, response)
        } catch (e: ApiException) {
            val errorCode = e.errorCode

            response.characterEncoding = StandardCharsets.UTF_8.name()
            response.contentType = MediaType.APPLICATION_JSON_VALUE
            response.status = errorCode.status.value()

            objectMapper.writeValue(response.writer, ApiResponse.fail(errorCode))
        }
    }
}
