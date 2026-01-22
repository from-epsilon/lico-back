package com.epsilon.nagginggnome.domain.push.repository.model

import com.epsilon.nagginggnome.domain.push.constant.MessageKind
import com.epsilon.nagginggnome.domain.push.constant.PushJobStatus
import java.time.Instant
import java.util.*

/**
 * 워커 처리용 조회 모델
 */
data class PushJobProcessingModel(

    /**
     * push_jobs PK
     */
    val id: Long,

    /**
     * 유저 ID
     */
    val userId: UUID,

    /**
     * 플랜 ID
     */
    val planId: Long?,

    /**
     * 메시지 종류
     */
    val kind: MessageKind,

    /**
     * 알림 제목
     */
    val title: String?,

    /**
     * 알림 내용
     */
    val body: String?,

    /**
     * 추가 데이터(JSON 문자열)
     */
    val dataJson: String?,

    /**
     * 발송 예정 시각
     */
    val scheduledAt: Instant,

    /**
     * 현재 작업 상태
     */
    val status: PushJobStatus
)
