package com.epsilon.nagginggnome.domain.push.controller

import com.epsilon.nagginggnome.domain.push.dto.request.FcmTokenUpsertRequest
import com.epsilon.nagginggnome.domain.push.service.FcmTokenService
import com.epsilon.nagginggnome.global.constant.code.CommonSuccessCode
import com.epsilon.nagginggnome.global.dto.response.ApiResponse
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

/**
 * 푸시 알림 관련 컨트롤러
 */
@RestController
@RequestMapping("/v1/push")
class PushController(
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
        fcmTokenService.deleteFcmTokensByUserId(userId)
        return ResponseEntity.ok(ApiResponse.success(CommonSuccessCode.SUCCESS))
    }
}
