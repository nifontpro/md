package ru.md.msc.rest.event.model.request

data class DeleteUserEventRequest(
	val authId: Long = 0,
	val eventId: Long = 0,
)