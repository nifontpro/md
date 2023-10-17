package ru.md.msc.rest.event.model.response

data class BaseEventResponse(
	val id: Long = 0,
	val eventDate: Long = 0,
	val days: Int = 0,
	val eventName: String = "",
	val entityName: String = "",
	val imageUrl: String? = null,
	val userId: Long = 0,
	val deptId: Long = 0,
	val deptName: String = "",
	val eventType: EventType,
	val deptLevel: Int
)
