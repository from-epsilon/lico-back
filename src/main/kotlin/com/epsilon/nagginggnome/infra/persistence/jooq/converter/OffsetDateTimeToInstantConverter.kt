package com.epsilon.nagginggnome.infra.persistence.jooq.converter

import org.jooq.impl.AbstractConverter
import java.time.Instant
import java.time.OffsetDateTime
import java.time.ZoneOffset

/**
 * jOOQ Converter
 *
 * - DB -> 앱: OffsetDateTime -> Instant
 * - 앱 -> DB: Instant -> OffsetDateTime(UTC)
 */
class OffsetDateTimeToInstantConverter :
    AbstractConverter<OffsetDateTime, Instant>(OffsetDateTime::class.java, Instant::class.java) {

    /**
     * DB 값을 애플리케이션 타입으로 변환
     * - OffsetDateTime은 오프셋이 포함된 값이므로 toInstant()로 절대 시각 변환
     */
    override fun from(databaseObject: OffsetDateTime?): Instant? =
        databaseObject?.toInstant()

    /**
     * 애플리케이션 값을 DB 타입으로 변환
     * - Instant는 오프셋이 없으므로 UTC 오프셋을 부여하여 OffsetDateTime으로 변환
     */
    override fun to(userObject: Instant?): OffsetDateTime? =
        userObject?.atOffset(ZoneOffset.UTC)
}
