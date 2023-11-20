package ru.md.msc.domain.event.model

import java.time.LocalDateTime

data class UserEvent(
	val id: Long = 0,
	val userId: Long = 0,
	val eventDate: LocalDateTime = LocalDateTime.now(),
	val eventName: String = "",
	val isUpdate: Boolean = false
)
