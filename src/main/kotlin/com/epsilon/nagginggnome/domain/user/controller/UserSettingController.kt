package com.epsilon.nagginggnome.domain.user.controller

import com.epsilon.nagginggnome.domain.user.dto.request.UserSettingCreateRequest
import com.epsilon.nagginggnome.domain.user.dto.request.UserSettingUpdateRequest
import com.epsilon.nagginggnome.domain.user.dto.response.UserSettingCreateResponse
import com.epsilon.nagginggnome.domain.user.dto.response.UserSettingGetResponse
import com.epsilon.nagginggnome.domain.user.dto.response.UserSettingUpdateResponse
import com.epsilon.nagginggnome.domain.user.service.UserSettingService
import com.epsilon.nagginggnome.global.constant.code.CommonSuccessCode
import com.epsilon.nagginggnome.global.dto.response.ApiResponse
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/v1/users/settings")
class UserSettingController(
    private val userSettingService: UserSettingService
) {

    @GetMapping
    fun getUserSetting(
        @AuthenticationPrincipal userId: UUID
    ): ResponseEntity<ApiResponse<UserSettingGetResponse>> {
        val res = userSettingService.getUserSetting(userId)
        return ResponseEntity.ok(ApiResponse.success(CommonSuccessCode.SUCCESS, res))
    }

    @PostMapping
    fun createUserSetting(
        @AuthenticationPrincipal userId: UUID,
        @RequestBody req: UserSettingCreateRequest
    ): ResponseEntity<ApiResponse<UserSettingCreateResponse>> {
        val res = userSettingService.createUserSetting(userId, req)
        return ResponseEntity.ok(ApiResponse.success(CommonSuccessCode.SUCCESS, res))
    }

    @PatchMapping
    fun updateUserSetting(
        @AuthenticationPrincipal userId: UUID,
        @RequestBody req: UserSettingUpdateRequest
    ): ResponseEntity<ApiResponse<UserSettingUpdateResponse>> {
        val res = userSettingService.updateUserSetting(userId, req)
        return ResponseEntity.ok(ApiResponse.success(CommonSuccessCode.SUCCESS, res))
    }
}
