package ru.md.msc.rest.event.model.request

data class AddUserEventRequest(
	val authId: Long = 0,
	val userId: Long = 0,
	val eventDate: Long = 0,
	val eventName: String = "",
)