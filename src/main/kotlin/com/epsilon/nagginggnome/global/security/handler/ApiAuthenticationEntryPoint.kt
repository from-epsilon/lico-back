package com.epsilon.nagginggnome.global.security.handler

import com.epsilon.nagginggnome.global.code.CommonErrorCode
import com.epsilon.nagginggnome.global.dto.response.ApiResponse
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.MediaType
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.stereotype.Component
import tools.jackson.databind.ObjectMapper
import java.nio.charset.StandardCharsets

/**
 * 인증이 없는 사용자가 보호 자원에 접근할 때(401) JSON 응답을 통일하는 엔트리 포인트
 */
@Component
class ApiAuthenticationEntryPoint(
    private val objectMapper: ObjectMapper
) : AuthenticationEntryPoint {
    override fun commence(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authException: AuthenticationException
    ) {
        if (response.isCommitted) return

        response.characterEncoding = StandardCharsets.UTF_8.name()
        response.contentType = MediaType.APPLICATION_JSON_VALUE
        response.status = HttpServletResponse.SC_UNAUTHORIZED

        val body = ApiResponse.fail(CommonErrorCode.UNAUTHORIZED)
        objectMapper.writeValue(response.writer, body)
    }

}
