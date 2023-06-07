package ru.md.msc.db.event.model

import java.time.LocalDateTime

interface IShortEvent {
	fun getId(): Long
	fun getEventDate(): LocalDateTime
	fun getDays(): Int
	fun getEventName(): String
}
