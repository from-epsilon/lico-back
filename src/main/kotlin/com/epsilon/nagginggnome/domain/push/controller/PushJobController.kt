package com.epsilon.nagginggnome.domain.push.controller

import com.epsilon.nagginggnome.domain.push.dto.request.PushJobUpsertRequest
import com.epsilon.nagginggnome.domain.push.service.PushJobService
import com.epsilon.nagginggnome.global.constant.code.CommonSuccessCode
import com.epsilon.nagginggnome.global.dto.response.ApiResponse
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
@RequestMapping("/v1/push")
class PushJobController(
    private val pushJobService: PushJobService
) {

    @PostMapping("/schedule")
    fun upsertPushJob(
        @AuthenticationPrincipal userId: UUID,
        @RequestBody req: PushJobUpsertRequest
    ): ResponseEntity<ApiResponse<Nothing>> {
        pushJobService.pushJobUpsert(userId, req)
        return ResponseEntity.ok(ApiResponse.success(CommonSuccessCode.SUCCESS))
    }
}
