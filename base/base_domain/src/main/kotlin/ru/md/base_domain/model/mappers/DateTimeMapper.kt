package ru.md.base_domain.model.mappers

import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset

fun Long.toLocalDateTimeUTC(): LocalDateTime {
	return LocalDateTime.ofInstant(Instant.ofEpochMilli(this), ZoneOffset.UTC)
}

fun LocalDateTime.toEpochMilliUTC() = this.atZone(ZoneOffset.UTC).toInstant().toEpochMilli()