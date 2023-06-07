package ru.md.msc.rest.event.model.response

data class ShortEventResponse(
	val id: Long = 0,
	val eventDate: Long = 0,
	val days: Int = 0,
	val eventName: String = "",
)
