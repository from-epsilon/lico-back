package com.epsilon.nagginggnome.global.config

import net.javacrumbs.shedlock.core.LockProvider
import net.javacrumbs.shedlock.provider.jdbctemplate.JdbcTemplateLockProvider
import net.javacrumbs.shedlock.spring.annotation.EnableSchedulerLock
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.scheduling.annotation.EnableScheduling

/**
 * batch 프로파일에서만 스케줄러를 활성화하기 위한 설정 클래스
 */
@Profile("batch")
@Configuration
@EnableScheduling
@EnableSchedulerLock(defaultLockAtMostFor = "PT10M")
class BatchSchedulingConfig(
    private val jdbcTemplate: JdbcTemplate
) {

    /**
     * ShedLock 분산락 Provider 빈
     */
    @Bean
    fun lockProvider(): LockProvider {
        return JdbcTemplateLockProvider(
            JdbcTemplateLockProvider.Configuration.builder()
                .withJdbcTemplate(jdbcTemplate)
                .withTableName("public.shedlock")
                .usingDbTime()
                .build()
        )
    }
}
