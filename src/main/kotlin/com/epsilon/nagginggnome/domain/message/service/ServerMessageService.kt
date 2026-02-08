package com.epsilon.nagginggnome.domain.message.service

import com.epsilon.nagginggnome.domain.message.dto.response.ServerMessageResponse
import com.epsilon.nagginggnome.domain.message.repository.ServerMessageRepository
import com.epsilon.nagginggnome.domain.plan.repository.PlanRepository
import com.epsilon.nagginggnome.global.constant.code.PlanErrorCode
import com.epsilon.nagginggnome.global.exception.ApiException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import tools.jackson.core.type.TypeReference
import tools.jackson.databind.ObjectMapper
import java.util.UUID

@Service
class ServerMessageService(
    private val planRepository: PlanRepository,
    private val serverMessageRepository: ServerMessageRepository,
    private val objectMapper: ObjectMapper
) {

    @Transactional(readOnly = true)
    fun getPlanServerMessages(userId: UUID, planId: UUID): List<ServerMessageResponse> {
        val plan = planRepository.findByIdAndUserId(planId = planId, userId = userId)
            ?: throw ApiException(PlanErrorCode.PLAN_NOT_FOUND)

        if (plan.isDeleted()) {
            throw ApiException(PlanErrorCode.PLAN_NOT_FOUND)
        }

        return serverMessageRepository.findByUserIdAndPlanId(userId, planId)
            .map { message ->
                val dataJson = message.dataJson?.let { objectMapper.readValue(it, dataJsonTypeRef) }
                ServerMessageResponse(
                    id = message.id,
                    planId = message.planId,
                    type = message.type,
                    createdAt = message.createdAt,
                    replyToId = null,
                    data = ServerMessageResponse.Payload(
                        title = message.title,
                        body = message.body,
                        dataJson = dataJson
                    )
                )
            }
    }

    companion object {
        private val dataJsonTypeRef = object : TypeReference<Map<String, Any?>>() {}
    }
}
