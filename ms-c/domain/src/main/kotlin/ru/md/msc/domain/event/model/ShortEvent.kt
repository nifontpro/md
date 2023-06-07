package ru.md.msc.domain.event.model

import java.time.LocalDateTime

data class ShortEvent(
	val id: Long = 0,
	val eventDate: LocalDateTime = LocalDateTime.now(),
	val days: Int = 0,
	val eventName: String = "",
)
