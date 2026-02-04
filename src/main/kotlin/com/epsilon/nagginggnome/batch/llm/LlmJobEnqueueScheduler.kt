package com.epsilon.nagginggnome.batch.llm

import net.javacrumbs.shedlock.spring.annotation.SchedulerLock
import org.springframework.context.annotation.Profile
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.time.Instant

/**
 * Llm 배치 스케줄러
 * - ShedLock으로 다중 인스턴스에서도 중복 실행을 방지
 */
@Profile("batch")
@Component
class LlmJobEnqueueScheduler(
    private val llmJobEnqueueRunner: LlmJobEnqueueRunner
) {

    /**
     * 매일 아침 실행
     */
    @SchedulerLock(
        name = "llmJobUserSummaryEnqueueScheduler",
        lockAtMostFor = "PT10M",
        lockAtLeastFor = "PT5S"
    )
    @Scheduled(
        cron = "0 0 9 * * *",
        zone = "Asia/Seoul"
    )
    fun runEveryMorning() {
        llmJobEnqueueRunner.runOnce(
            now = Instant.now(),
            limit = 500
        )
    }
}
