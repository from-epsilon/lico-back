package com.epsilon.nagginggnome.domain.message.controller

import com.epsilon.nagginggnome.domain.message.dto.request.UserMessageCreateRequest
import com.epsilon.nagginggnome.domain.message.dto.request.UserMessageUpdateRequest
import com.epsilon.nagginggnome.domain.message.dto.response.ServerMessageResponse
import com.epsilon.nagginggnome.domain.message.service.ServerMessageService
import com.epsilon.nagginggnome.domain.message.service.UserMessageService
import com.epsilon.nagginggnome.global.constant.code.CommonSuccessCode
import com.epsilon.nagginggnome.global.dto.response.ApiResponse
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

/**
 * 플랜 메시지 컨트롤러
 */
@RestController
@RequestMapping("/v1/plans")
class PlanMessageController(
    private val serverMessageService: ServerMessageService,
    private val userMessageService: UserMessageService
) {

    @GetMapping("/{planId}/messages")
    fun getPlanServerMessages(
        @AuthenticationPrincipal userId: UUID,
        @PathVariable planId: UUID
    ): ResponseEntity<ApiResponse<List<ServerMessageResponse>>> {
        val res = serverMessageService.getPlanServerMessages(
            userId = userId,
            planId = planId
        )
        return ResponseEntity.ok(
            ApiResponse.success(
                successCode = CommonSuccessCode.SUCCESS,
                data = res
            )
        )
    }

    @PostMapping("/{planId}/messages")
    fun createUserMessage(
        @AuthenticationPrincipal userId: UUID,
        @PathVariable planId: UUID,
        @RequestBody req: UserMessageCreateRequest
    ): ResponseEntity<ApiResponse<Nothing>> {
        userMessageService.create(
            userId = userId,
            planId = planId,
            req = req
        )
        return ResponseEntity.ok(
            ApiResponse.success(
                successCode = CommonSuccessCode.SUCCESS
            )
        )
    }

    @PutMapping("/{planId}/messages/{messageId}")
    fun updateUserMessage(
        @AuthenticationPrincipal userId: UUID,
        @PathVariable planId: UUID,
        @PathVariable messageId: UUID,
        @RequestBody req: UserMessageUpdateRequest
    ): ResponseEntity<ApiResponse<Nothing>> {
        userMessageService.update(
            userId = userId,
            planId = planId,
            messageId = messageId,
            req = req
        )
        return ResponseEntity.ok(
            ApiResponse.success(
                successCode = CommonSuccessCode.SUCCESS
            )
        )
    }
}
