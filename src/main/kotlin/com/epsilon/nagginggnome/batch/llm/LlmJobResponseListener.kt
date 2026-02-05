package com.epsilon.nagginggnome.batch.llm

import com.epsilon.nagginggnome.domain.llm.service.LlmJobService
import jakarta.annotation.PostConstruct
import jakarta.annotation.PreDestroy
import org.postgresql.PGConnection
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component
import java.sql.Connection
import java.time.Instant
import java.util.concurrent.Executor
import javax.sql.DataSource

@Profile("batch")
@Component
class LlmJobResponseListener(
    private val dataSource: DataSource,
    private val llmJobService: LlmJobService,

    @Qualifier("llmJobListenerExecutor")
    private val executor: Executor
) {

    private val channelName = "llm_job_response"

    @Volatile
    private var running = true

    @Volatile
    private var listenConnection: Connection? = null

    @PostConstruct
    fun start() {
        // 단일 스레드 executor에 리스너 실행을 위임
        executor.execute { supervisorLoop() }
    }

    @PreDestroy
    fun stop() {
        running = false
        // 커넥션 close로 블로킹 대기를 깨워 종료
        runCatching { listenConnection?.close() }
    }

    private fun supervisorLoop() {
        while (running) {
            runCatching { listenLoopOnce() }
            if (!running) return

            runCatching { Thread.sleep(500L) }
        }
    }

    private fun listenLoopOnce() {
        dataSource.connection.use { conn ->
            listenConnection = conn
            conn.autoCommit = true

            conn.createStatement().use { stmt ->
                stmt.execute("LISTEN $channelName")
            }

            val pgConn = conn.unwrap(PGConnection::class.java)

            while (running) {
                val notification = pgConn.getNotifications(10_000)?.firstOrNull() ?: continue
                val id = notification.parameter?.toLongOrNull() ?: continue
                llmJobService.applySuccessJobById(Instant.now(), id)
            }
        }
    }
}
