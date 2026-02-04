package com.epsilon.nagginggnome.domain.push.repository.model

import com.epsilon.nagginggnome.domain.push.constant.MessageKind
import com.epsilon.nagginggnome.domain.push.constant.PushJobStatus
import java.time.Instant
import java.util.UUID

/**
 * 배치 삽입에 필요한 입력 모델
 */
data class PushJobCreateModel(

    /**
     * 유저 ID
     */
    val userId: UUID,

    /**
     * 관련 플랜 ID
     * - general인 경우 null
     */
    val planId: UUID?,

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
     * 작업 상태
     * - 신규 적재는 READY 고정
     */
    val status: PushJobStatus = PushJobStatus.READY,

    /**
     * 발송 예정 시각
     */
    val scheduledAt: Instant
)
