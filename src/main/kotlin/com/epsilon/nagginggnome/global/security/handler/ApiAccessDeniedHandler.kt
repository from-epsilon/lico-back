package com.epsilon.nagginggnome.global.security.handler

import com.epsilon.nagginggnome.global.code.CommonErrorCode
import com.epsilon.nagginggnome.global.dto.response.ApiResponse
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.MediaType
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.web.access.AccessDeniedHandler
import org.springframework.stereotype.Component
import tools.jackson.databind.ObjectMapper
import java.nio.charset.StandardCharsets

/**
 * 인증은 되었으나 권한이 부족할 때(403) JSON 응답을 통일하는 핸들러
 */
@Component
class ApiAccessDeniedHandler(
    private val objectMapper: ObjectMapper
) : AccessDeniedHandler {
    override fun handle(
        request: HttpServletRequest,
        response: HttpServletResponse,
        accessDeniedException: AccessDeniedException
    ) {
        if (response.isCommitted) return

        response.characterEncoding = StandardCharsets.UTF_8.name()
        response.contentType = MediaType.APPLICATION_JSON_VALUE
        response.status = HttpServletResponse.SC_FORBIDDEN

        val body = ApiResponse.fail(CommonErrorCode.FORBIDDEN)
        objectMapper.writeValue(response.writer, body)
    }
}
