package com.epsilon.nagginggnome.domain.push.util

import net.fortuna.ical4j.model.Recur
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.zone.ZoneOffsetTransition

object PushRRuleUtils {
    fun nextOccurrence(rrule: String, dtStart: ZonedDateTime, after: ZonedDateTime): ZonedDateTime? {
        val recur = Recur<ZonedDateTime>(rrule)
        return recur.getNextDate(dtStart, after)?.let { resolveImaginaryTime(it) }
    }

    fun nextOccurrence(
        rrule: String,
        dtStart: Instant,
        zoneId: ZoneId,
        after: Instant = dtStart
    ): Instant? {
        val dtStartZoned = ZonedDateTime.ofInstant(dtStart, zoneId)
        val afterZoned = ZonedDateTime.ofInstant(after, zoneId)
        return nextOccurrence(rrule, dtStartZoned, afterZoned)?.toInstant()
    }

    private fun resolveImaginaryTime(dateTime: ZonedDateTime): ZonedDateTime {
        val rules = dateTime.zone.rules
        val localDateTime = dateTime.toLocalDateTime()
        val validOffsets = rules.getValidOffsets(localDateTime)
        if (validOffsets.isNotEmpty()) {
            return dateTime
        }

        val transition: ZoneOffsetTransition = rules.getTransition(localDateTime) ?: return dateTime
        return ZonedDateTime.ofLocal(
            transition.dateTimeAfter,
            dateTime.zone,
            transition.offsetAfter
        )
    }
}
