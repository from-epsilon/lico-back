package com.epsilon.nagginggnome.domain.push.config

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.messaging.FirebaseMessaging
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.io.FileInputStream

@Configuration
class FirebaseAdminConfig(
    private val props: FcmProperties
) {
    @Bean
    fun firebaseApp(): FirebaseApp {

        /**
         * 이미 FirebaseApp이 초기화되어 있으면 재사용
         */
        val existingApps = FirebaseApp.getApps()
        if (existingApps.isNotEmpty()) {
            return existingApps.firstOrNull { it.name == FirebaseApp.DEFAULT_APP_NAME }
                ?: existingApps.first()
        }

        /**
         * 서비스 계정 JSON 파일을 읽어 FirebaseOptions를 생성한 뒤 FirebaseApp을 초기화
         */
        FileInputStream(props.credentialsPath).use { input ->
            return FirebaseApp.initializeApp(
                FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(input))
                    .build()
            )
        }
    }

    /**
     * FirebaseApp에 연결된 FirebaseMessaging 싱글턴을 제공하는 빈
     * - 이후 서비스 계층에서 이 빈을 주입받아 send() 호출로 메시지를 발송
     */
    @Bean
    fun firebaseMessaging(firebaseApp: FirebaseApp): FirebaseMessaging {
        return FirebaseMessaging.getInstance(firebaseApp)
    }
}
