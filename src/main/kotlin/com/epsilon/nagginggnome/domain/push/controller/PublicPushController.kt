package com.epsilon.nagginggnome.domain.push.controller

import com.epsilon.nagginggnome.domain.push.dto.request.PublicFcmTokenRequest
import com.epsilon.nagginggnome.global.constant.code.CommonErrorCode
import com.epsilon.nagginggnome.global.constant.code.CommonSuccessCode
import com.epsilon.nagginggnome.global.dto.response.ApiResponse
import com.epsilon.nagginggnome.global.exception.ApiException
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * 공개 테스트용 푸시 엔드포인트
 */
@RestController
@RequestMapping("/v1/public")
class PublicPushController {

    @PostMapping("/fcm-tokens")
    fun submitFcmToken(
        @RequestBody req: PublicFcmTokenRequest
    ): ResponseEntity<ApiResponse<Nothing>> {
        if (req.fcmToken.isBlank()) {
            throw ApiException(
                errorCode = CommonErrorCode.BAD_REQUEST
            )
        }

        return ResponseEntity.ok(
            ApiResponse.success(
                successCode = CommonSuccessCode.SUCCESS
            )
        )
    }
}
