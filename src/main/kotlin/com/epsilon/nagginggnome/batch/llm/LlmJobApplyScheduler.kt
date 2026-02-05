package com.epsilon.nagginggnome.batch.llm

import net.javacrumbs.shedlock.spring.annotation.SchedulerLock
import org.springframework.context.annotation.Profile
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.time.Instant

@Profile("batch")
@Component
class LlmJobApplyScheduler(
    private val llmJobApplyRunner: LlmJobApplyRunner
) {

    @SchedulerLock(
        name = "llmJobApplyScheduler",
        lockAtMostFor = "PT10M",
        lockAtLeastFor = "PT5S"
    )
    @Scheduled(cron = "0 * * * * *", zone = "Asia/Seoul")
    fun runEveryMinute() {
        llmJobApplyRunner.runOnce(
            now = Instant.now(),
            limit = 100
        )
    }
}
