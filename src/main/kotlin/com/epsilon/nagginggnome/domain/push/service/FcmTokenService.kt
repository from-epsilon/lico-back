package com.epsilon.nagginggnome.domain.push.service

import com.epsilon.nagginggnome.domain.push.dto.request.FcmTokenUpsertRequest
import com.epsilon.nagginggnome.domain.push.entity.FcmToken
import com.epsilon.nagginggnome.domain.push.repository.FcmTokenRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Instant
import java.util.*

@Service
class FcmTokenService(
    private val fcmTokenRepository: FcmTokenRepository
) {

    /**
     * FCM 토큰을 업서트(없으면 insert, 있으면 update)
     *
     * 핵심 정책
     * - 유저 1명당 1개 토큰만 유지합니다(UNIQUE user_id).
     * - 마지막 로그인한 기기의 token으로 덮어쓰기
     */
    @Transactional
    fun upsertFcmToken(userId: UUID, req: FcmTokenUpsertRequest) {
        val now = Instant.now()
        val token = req.fcmToken

        // 동일 token이 다른 유저에게 붙어있다면 제거
        detachTokenFromOtherUser(currentUserId = userId, token = token)

        /**
         * 현재 유저의 토큰 조회
         * - 없으면 생성
         * - 있으면 갱신
         */
        val fcmToken = fcmTokenRepository.findByUserId(userId)
            ?: run {
                fcmTokenRepository.save(
                    FcmToken(
                        userId = userId,
                        token = token,
                        lastLoginAt = now
                    )
                )
                return
            }

        //  기존 토큰 갱신
        fcmToken.updateToLastLogin(newToken = token, newLastLoginAt = now)
    }

    /**
     * 동일 토큰이 다른 유저에게 묶여있는 경우 하드 삭제합
     */
    private fun detachTokenFromOtherUser(currentUserId: UUID, token: String) {
        // 토큰이 존재하지 않으면 정상 케이스
        val fcmToken = fcmTokenRepository.findByToken(token) ?: return

        // fcmToken.userId가 currentUserId와 같으면 정상 케이스
        if (fcmToken.userId == currentUserId) return

        // 다른 유저가 동일 토큰을 들고 있으면 제거
        fcmTokenRepository.delete(fcmToken)
    }

    /**
     * 유저의 FCM 토큰 삭제
     * - 하드 딜리트
     */
    @Transactional
    fun deleteFcmToken(userId: UUID) {
        fcmTokenRepository.deleteByUserId(userId)
    }
}
