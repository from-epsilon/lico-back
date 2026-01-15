package com.epsilon.nagginggnome.domain.auth.controller

import com.epsilon.nagginggnome.domain.auth.dto.request.LogoutRequest
import com.epsilon.nagginggnome.domain.auth.dto.request.TokenReissueRequest
import com.epsilon.nagginggnome.domain.auth.dto.response.TokenReissueResponse
import com.epsilon.nagginggnome.domain.auth.service.AuthService
import com.epsilon.nagginggnome.global.constant.code.CommonSuccessCode
import com.epsilon.nagginggnome.global.dto.response.ApiResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * 인증 관련 중앙 컨트롤러
 */
@RestController
@RequestMapping("/v1/auth")
class AuthController(
    private val authService: AuthService
) {

    /**
     * Access Token 재발급
     */
    @PostMapping("/token/reissue")
    fun reissue(
        @RequestBody req: TokenReissueRequest
    ): ResponseEntity<ApiResponse<TokenReissueResponse>> {
        val res = authService.reissue(req)
        return ResponseEntity.ok(ApiResponse.ok(CommonSuccessCode.SUCCESS, res))
    }

    /**
     * 로그아웃
     */
    @PostMapping("/logout")
    fun logout(
        @RequestBody req: LogoutRequest
    ): ResponseEntity<ApiResponse<Nothing>> {
        authService.logout(req)
        return ResponseEntity.ok(ApiResponse.ok(CommonSuccessCode.SUCCESS))
    }
}
