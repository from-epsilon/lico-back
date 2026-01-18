package com.epsilon.nagginggnome.domain.message.controller

import com.epsilon.nagginggnome.domain.message.dto.request.ChatMessageCreateRequest
import com.epsilon.nagginggnome.domain.message.dto.response.ChatMessageListItemResponse
import com.epsilon.nagginggnome.domain.message.service.ChatMessageService
import com.epsilon.nagginggnome.global.constant.code.CommonSuccessCode
import com.epsilon.nagginggnome.global.dto.response.ApiResponse
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import java.util.*

/**
 * 플랜 채팅 메시지 컨트롤러
 */
@RestController
@RequestMapping("/v1/plans")
class PlanChatMessageController(
    private val chatMessageService: ChatMessageService
) {
    @GetMapping("/{planId}/messages")
    fun getPlanChatMessages(
        @AuthenticationPrincipal userId: UUID,
        @PathVariable planId: Long,
        @PageableDefault(size = 20) pageable: Pageable
    ): ResponseEntity<ApiResponse<Page<ChatMessageListItemResponse>>> {
        val res = chatMessageService.getPlanChatMessages(userId, planId, pageable)
        return ResponseEntity.ok(ApiResponse.success(CommonSuccessCode.SUCCESS, res))
    }

    @PostMapping("/{planId}/messages")
    fun createPlanChatMessage(
        @AuthenticationPrincipal userId: UUID,
        @PathVariable planId: Long,
        @RequestBody req: ChatMessageCreateRequest
    ): ResponseEntity<ApiResponse<Nothing>> {
        chatMessageService.appendUserReply(userId, planId, req)
        return ResponseEntity.ok(ApiResponse.success(CommonSuccessCode.SUCCESS))
    }
}
