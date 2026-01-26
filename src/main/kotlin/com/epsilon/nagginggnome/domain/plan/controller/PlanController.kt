package com.epsilon.nagginggnome.domain.plan.controller

import com.epsilon.nagginggnome.domain.plan.dto.request.PlanCreateRequest
import com.epsilon.nagginggnome.domain.plan.dto.request.PlanUpdateRequest
import com.epsilon.nagginggnome.domain.plan.dto.response.PlanUpsertResponse
import com.epsilon.nagginggnome.domain.plan.service.PlanService
import com.epsilon.nagginggnome.global.constant.code.CommonSuccessCode
import com.epsilon.nagginggnome.global.dto.response.ApiResponse
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import java.util.*

/**
 * 플랜 컨트롤러
 */
@RestController
@RequestMapping("/v1/plans")
class PlanController(
    private val planService: PlanService
) {

    /**
     * 플랜 생성 API
     */
    @PostMapping
    fun createPlan(
        @AuthenticationPrincipal userId: UUID,
        @RequestBody req: PlanCreateRequest
    ): ResponseEntity<ApiResponse<PlanUpsertResponse>> {
        val res = planService.createPlan(userId, req)
        return ResponseEntity.ok(ApiResponse.success(CommonSuccessCode.SUCCESS, res))
    }

    /**
     * 플랜 수정 API
     */
    @PostMapping("/{planId}")
    fun updatePlan(
        @AuthenticationPrincipal userId: UUID,
        @PathVariable planId: UUID,
        @RequestBody req: PlanUpdateRequest
    ): ResponseEntity<ApiResponse<PlanUpsertResponse>> {
        val res = planService.updatePlan(userId, planId, req)
        return ResponseEntity.ok(ApiResponse.success(CommonSuccessCode.SUCCESS, res))
    }

    /**
     * 플랜 삭제 API
     */
    @DeleteMapping("/{planId}")
    fun deletePlan(
        @AuthenticationPrincipal userId: UUID,
        @PathVariable planId: UUID
    ): ResponseEntity<ApiResponse<Nothing>> {
        planService.deletePlan(userId, planId)
        return ResponseEntity.ok(ApiResponse.success(CommonSuccessCode.SUCCESS))
    }
}
