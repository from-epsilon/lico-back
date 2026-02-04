package com.epsilon.nagginggnome.domain.push.service

import com.epsilon.nagginggnome.domain.push.constant.MessageKind
import com.epsilon.nagginggnome.domain.push.constant.PushJobStatus
import com.epsilon.nagginggnome.domain.push.dto.request.PushJobUpsertRequest
import com.epsilon.nagginggnome.domain.push.repository.PushJobRepository
import com.epsilon.nagginggnome.domain.push.repository.model.PushJobCreateModel
import com.epsilon.nagginggnome.global.constant.code.PushJobErrorCode
import com.epsilon.nagginggnome.global.exception.ApiException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
class PushJobService(
    private val pushJobRepository: PushJobRepository
) {

    /**
     * 업로드된 스케줄을 DB에 덮어쓰기(upsert)
     *
     * - 배치 키 멱등 삽입 성공 시에만 range 삭제 및 신규 insert 수행
     * - 이미 처리된 batchId면 아무것도 하지 않음
     */
    @Transactional
    fun pushJobUpsert(userId: UUID, req: PushJobUpsertRequest) {

        /**
         * 배치 키를 멱등하게 삽입
         * - false면 이미 처리된 요청으로 간주하고 종료
         */
        val inserted: Boolean = pushJobRepository.tryInsertBatchKey(userId, req.batchId)
        if (!inserted) {
            return
        }

        /**
         * 기존 READY 작업을 삭제
         * - [from, to]
         */
        pushJobRepository.deletePushJobByUserAndRange(userId, req.range.from, req.range.to)

        /**
         * notices를 CreateModel 리스트로 변환 후 배치 INSERT
         */
        val createModels: List<PushJobCreateModel> = req.notices.map { notice ->
            val kind: MessageKind = MessageKind.from(notice.kind)
                ?: throw ApiException(PushJobErrorCode.INVALID_MESSAGE_KIND)

            PushJobCreateModel(
                userId = userId,
                planId = notice.planId,
                kind = kind,
                title = null,
                body = null,
                dataJson = null,
                status = PushJobStatus.READY,
                scheduledAt = notice.scheduledAt
            )
        }
        pushJobRepository.insertPushJob(userId, req.batchId, createModels)
    }
}
