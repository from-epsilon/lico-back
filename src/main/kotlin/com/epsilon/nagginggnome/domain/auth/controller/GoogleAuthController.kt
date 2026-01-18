package com.epsilon.nagginggnome.domain.auth.controller

import com.epsilon.nagginggnome.domain.auth.dto.request.SocialLoginRequest
import com.epsilon.nagginggnome.domain.auth.dto.response.SocialLoginResponse
import com.epsilon.nagginggnome.domain.auth.service.GoogleAuthService
import com.epsilon.nagginggnome.global.constant.code.CommonSuccessCode
import com.epsilon.nagginggnome.global.dto.response.ApiResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/auth/social/google")
class GoogleAuthController(
    private val googleAuthService: GoogleAuthService
) {

    /**
     * 소셜 로그인 API
     */
    @PostMapping("/login")
    fun login(
        @RequestBody req: SocialLoginRequest
    ): ResponseEntity<ApiResponse<SocialLoginResponse>> {
        val res = googleAuthService.loginOrSignUp(req)
        return ResponseEntity.ok(ApiResponse.success(CommonSuccessCode.SUCCESS, res))
    }
}
