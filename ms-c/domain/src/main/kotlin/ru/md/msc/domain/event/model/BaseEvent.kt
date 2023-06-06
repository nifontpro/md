package ru.md.msc.domain.event.model

import java.time.LocalDateTime

data class BaseEvent(
	val id: Long = 0,
	val eventDate: LocalDateTime = LocalDateTime.now(),
	val eventName: String = "",
	val entityName: String = "",
	val imageUrl: String? = null,
	val userId: Long = 0,
	val deptId: Long = 0,
	val deptName: String = "",
)
