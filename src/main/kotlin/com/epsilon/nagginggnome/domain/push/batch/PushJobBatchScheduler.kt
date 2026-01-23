package com.epsilon.nagginggnome.domain.push.batch

import net.javacrumbs.shedlock.spring.annotation.SchedulerLock
import org.springframework.context.annotation.Profile
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.time.Instant

/**
 * Push 배치 스케줄러
 * - ShedLock으로 다중 인스턴스에서도 중복 실행을 방지
 */
@Profile("batch")
@Component
class PushJobBatchScheduler(
    private val pushJobBatchRunner: PushJobBatchRunner
) {

    /**
     * 매 1분마다 실행(매 분 0초).
     */
    @SchedulerLock(
        name = "pushJobBatchScheduler",
        lockAtMostFor = "PT10M",
        lockAtLeastFor = "PT5S"
    )
    @Scheduled(cron = "0 * * * * *", zone = "Asia/Seoul")
    fun runEveryMinute() {
        pushJobBatchRunner.runOnce(
            now = Instant.now(),
            limit = 100
        )
    }
}
