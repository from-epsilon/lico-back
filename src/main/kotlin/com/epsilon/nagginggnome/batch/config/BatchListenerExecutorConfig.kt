package com.epsilon.nagginggnome.batch.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor
import java.util.concurrent.Executor

/**
 * batch 프로필에서만 동작하는 리스너 전용 TaskExecutor 설정
 */
@Profile("batch")
@Configuration
class BatchListenerExecutorConfig {

    @Bean
    fun llmJobListenerExecutor(): Executor {
        return ThreadPoolTaskExecutor().apply {
            corePoolSize = 1
            maxPoolSize = 1
            queueCapacity = 0
            setThreadNamePrefix("llm-job-listener-")
            setWaitForTasksToCompleteOnShutdown(true)
            setAwaitTerminationSeconds(10)
            initialize()
        }
    }
}
