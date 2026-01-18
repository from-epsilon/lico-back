package com.epsilon.nagginggnome.domain.plan.controller

import com.epsilon.nagginggnome.domain.plan.dto.request.PlanCreateRequest
import com.epsilon.nagginggnome.domain.plan.dto.request.PlanUpdateRequest
import com.epsilon.nagginggnome.domain.plan.dto.response.PlanCreateResponse
import com.epsilon.nagginggnome.domain.plan.dto.response.PlanDetailResponse
import com.epsilon.nagginggnome.domain.plan.dto.response.PlanListItemResponse
import com.epsilon.nagginggnome.domain.plan.dto.response.PlanUpdateResponse
import com.epsilon.nagginggnome.domain.plan.service.PlanService
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
 * 플랜 컨트롤러
 */
@RestController
@RequestMapping("/v1/plans")
class PlanController(
    private val planService: PlanService
) {

    /**
     * 플랜 리스트 조회 API
     */
    @GetMapping
    fun getPlanList(
        @AuthenticationPrincipal userId: UUID,
        @PageableDefault(size = 20) pageable: Pageable
    ): ResponseEntity<ApiResponse<Page<PlanListItemResponse>>> {
        val res = planService.getMyPlans(userId, pageable)
        return ResponseEntity.ok(ApiResponse.success(CommonSuccessCode.SUCCESS, res))
    }

    /**
     * 플랜 상세 조회 API
     */
    @GetMapping("/{planId}")
    fun getPlan(
        @AuthenticationPrincipal userId: UUID,
        @PathVariable planId: Long
    ): ResponseEntity<ApiResponse<PlanDetailResponse>> {
        val res = planService.getPlanDetail(userId, planId)
        return ResponseEntity.ok(ApiResponse.success(CommonSuccessCode.SUCCESS, res))
    }

    /**
     * 플랜 스냅샷 버전 목록 조회 API
     */
    @GetMapping("/{planId}/snapshots/versions")
    fun getSnapshotVersions(
        @AuthenticationPrincipal userId: UUID,
        @PathVariable planId: Long
    ): ResponseEntity<ApiResponse<List<Int>>> {
        val res = planService.getSnapshotVersions(userId, planId)
        return ResponseEntity.ok(ApiResponse.success(CommonSuccessCode.SUCCESS, res))
    }

    /**
     * 플랜 특정 버전 상세 조회 API
     */
    @GetMapping("/{planId}/snapshots/{version}")
    fun getPlanVersion(
        @AuthenticationPrincipal userId: UUID,
        @PathVariable planId: Long,
        @PathVariable version: Int
    ): ResponseEntity<ApiResponse<PlanDetailResponse>> {
        val res = planService.getPlanVersionDetail(userId, planId, version)
        return ResponseEntity.ok(ApiResponse.success(CommonSuccessCode.SUCCESS, res))
    }

    /**
     * 플랜 생성 API
     */
    @PostMapping
    fun createPlan(
        @AuthenticationPrincipal userId: UUID,
        @RequestBody req: PlanCreateRequest
    ): ResponseEntity<ApiResponse<PlanCreateResponse>> {
        val res = planService.createPlan(userId, req)
        return ResponseEntity.ok(ApiResponse.success(CommonSuccessCode.SUCCESS, res))
    }

    /**
     * 플랜 수정 API
     */
    @PostMapping("/{planId}")
    fun updatePlan(
        @AuthenticationPrincipal userId: UUID,
        @PathVariable planId: Long,
        @RequestBody req: PlanUpdateRequest
    ): ResponseEntity<ApiResponse<PlanUpdateResponse>> {
        val res = planService.updatePlan(userId, planId, req)
        return ResponseEntity.ok(ApiResponse.success(CommonSuccessCode.SUCCESS, res))
    }

    /**
     * 플랜 삭제 API
     */
    @DeleteMapping("/{planId}")
    fun deletePlan(
        @AuthenticationPrincipal userId: UUID,
        @PathVariable planId: Long
    ): ResponseEntity<ApiResponse<Nothing>> {
        planService.deletePlan(userId, planId)
        return ResponseEntity.ok(ApiResponse.success(CommonSuccessCode.SUCCESS))
    }
}
