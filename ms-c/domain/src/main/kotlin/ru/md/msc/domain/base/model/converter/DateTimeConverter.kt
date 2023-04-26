package ru.md.msc.domain.base.model.converter

import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset

fun Long.toLocalDateTimeUTC(): LocalDateTime {
	return LocalDateTime.ofInstant(Instant.ofEpochMilli(this), ZoneOffset.UTC)
}

fun LocalDateTime.toEpochMilliUTC() = this.atZone(ZoneOffset.UTC).toInstant().toEpochMilli()