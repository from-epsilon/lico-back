package com.epsilon.nagginggnome.domain.user.controller

import com.epsilon.nagginggnome.domain.user.dto.request.UserDetailCreateRequest
import com.epsilon.nagginggnome.domain.user.dto.request.UserDetailUpdateRequest
import com.epsilon.nagginggnome.domain.user.dto.response.UserDetailGetResponse
import com.epsilon.nagginggnome.domain.user.service.UserDetailService
import com.epsilon.nagginggnome.global.constant.code.CommonSuccessCode
import com.epsilon.nagginggnome.global.dto.response.ApiResponse
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/v1/users/details")
class UserDetailController(
    private val userDetailService: UserDetailService
) {

    @GetMapping
    fun getUserDetail(
        @AuthenticationPrincipal userId: UUID
    ): ResponseEntity<ApiResponse<UserDetailGetResponse>> {
        val res = userDetailService.getUserDetail(userId)
        return ResponseEntity.ok(ApiResponse.success(CommonSuccessCode.SUCCESS, res))
    }

    @PostMapping
    fun createUserDetail(
        @AuthenticationPrincipal userId: UUID,
        @RequestBody req: UserDetailCreateRequest
    ): ResponseEntity<ApiResponse<Nothing>> {
        userDetailService.createUserDetail(userId, req)
        return ResponseEntity.ok(ApiResponse.success(CommonSuccessCode.SUCCESS))
    }

    @PatchMapping
    fun updateUserDetail(
        @AuthenticationPrincipal userId: UUID,
        @RequestBody req: UserDetailUpdateRequest
    ): ResponseEntity<ApiResponse<Nothing>> {
        userDetailService.updateUserDetail(userId, req)
        return ResponseEntity.ok(ApiResponse.success(CommonSuccessCode.SUCCESS))
    }
}
