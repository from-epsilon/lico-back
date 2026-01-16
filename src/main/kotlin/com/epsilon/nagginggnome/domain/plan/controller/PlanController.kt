package com.epsilon.nagginggnome.domain.plan.controller

import com.epsilon.nagginggnome.domain.plan.dto.request.PlanCreateRequest
import com.epsilon.nagginggnome.domain.plan.dto.request.PlanUpdateRequest
import com.epsilon.nagginggnome.domain.plan.dto.response.PlanListItemResponse
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
@RequestMapping("v1/plans")
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
}
