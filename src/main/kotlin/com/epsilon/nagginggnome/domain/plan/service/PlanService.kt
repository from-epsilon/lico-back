package com.epsilon.nagginggnome.domain.plan.service

import com.epsilon.nagginggnome.domain.plan.dto.response.PlanListItemResponse
import com.epsilon.nagginggnome.domain.plan.repository.PlanRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class PlanService(
    private val planRepository: PlanRepository
) {

    /**
     * 내 플랜 목록 조회
     */
    @Transactional(readOnly = true)
    fun getMyPlans(userId: UUID, pageable: Pageable): Page<PlanListItemResponse> {
        return planRepository.findMyPlans(userId, pageable)
    }
}
