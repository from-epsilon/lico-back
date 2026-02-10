package com.epsilon.nagginggnome.domain.message.controller

import com.epsilon.nagginggnome.domain.message.dto.response.ServerMessageResponse
import com.epsilon.nagginggnome.domain.message.service.ServerMessageService
import com.epsilon.nagginggnome.global.constant.code.CommonSuccessCode
import com.epsilon.nagginggnome.global.dto.response.ApiResponse
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

/**
 * 전역 서버 메시지 컨트롤러
 */
@RestController
@RequestMapping("/v1/messages")
class ServerMessageController(
    private val serverMessageService: ServerMessageService
) {

    @GetMapping
    fun getGlobalServerMessages(
        @AuthenticationPrincipal userId: UUID
    ): ResponseEntity<ApiResponse<List<ServerMessageResponse>>> {
        val res = serverMessageService.getGlobalServerMessages(userId)
        return ResponseEntity.ok(
            ApiResponse.success(
                successCode = CommonSuccessCode.SUCCESS,
                data = res
            )
        )
    }
}
