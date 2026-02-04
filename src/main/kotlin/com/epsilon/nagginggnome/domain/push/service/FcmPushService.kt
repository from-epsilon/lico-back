package com.epsilon.nagginggnome.domain.push.service

import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingException
import com.google.firebase.messaging.Message
import com.google.firebase.messaging.MessagingErrorCode
import com.google.firebase.messaging.Notification
import org.springframework.stereotype.Service

/**
 * FCM 푸시 발송 서비스
 */
@Service
class FcmPushService(
    private val firebaseMessaging: FirebaseMessaging,
    private val fcmTokenService: FcmTokenService
) {

    /**
     * 단일 토큰 발송
     */
    fun sendToToken(
        token: String,
        title: String? = null,
        body: String? = null,
        data: Map<String, String> = emptyMap()
    ): String {

        // 메시지 구성
        val message = Message.builder()
            .setToken(token)
            .apply {
                buildNotificationOrNull(title, body)
                    ?.let(::setNotification)
                data.takeIf { it.isNotEmpty() }
                    ?.let(::putAllData)
            }
            .build()

        return try {
            firebaseMessaging.send(message)
        } catch (e: FirebaseMessagingException) {

            // UNREGISTERED: 더 이상 유효하지 않은 토큰
            if (e.messagingErrorCode == MessagingErrorCode.UNREGISTERED) {
                fcmTokenService.deleteFcmTokenByToken(token)
            }
            throw e
        }
    }

    /**
     * title/body가 모두 null이면 Notification을 생략하기 위해 null 반환
     */
    private fun buildNotificationOrNull(title: String?, body: String?): Notification? {
        if (title == null && body == null) return null

        return Notification.builder().apply {
            title?.let { setTitle(it) }
            body?.let { setBody(it) }
        }.build()
    }
}
