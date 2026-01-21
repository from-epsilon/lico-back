package com.epsilon.nagginggnome.domain.push.controller

import com.epsilon.nagginggnome.domain.push.dto.request.FcmTokenUpsertRequest
import com.epsilon.nagginggnome.domain.push.service.FcmTokenService
import com.epsilon.nagginggnome.global.constant.code.CommonSuccessCode
import com.epsilon.nagginggnome.global.dto.response.ApiResponse
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import java.util.*

/**
 * FCM Token 컨트롤러
 */
@RestController
@RequestMapping("/v1/push")
class FcmTokenController(
    private val fcmTokenService: FcmTokenService
) {

    @PostMapping("/token")
    fun upsertToken(
        @AuthenticationPrincipal userId: UUID,
        @RequestBody req: FcmTokenUpsertRequest
    ): ResponseEntity<ApiResponse<Nothing>> {
        fcmTokenService.upsertFcmToken(userId, req)
        return ResponseEntity.ok(ApiResponse.success(CommonSuccessCode.SUCCESS))
    }

    @DeleteMapping("/token")
    fun deleteToken(
        @AuthenticationPrincipal userId: UUID
    ): ResponseEntity<ApiResponse<Nothing>> {
        fcmTokenService.deleteFcmToken(userId)
        return ResponseEntity.ok(ApiResponse.success(CommonSuccessCode.SUCCESS))
    }
}
