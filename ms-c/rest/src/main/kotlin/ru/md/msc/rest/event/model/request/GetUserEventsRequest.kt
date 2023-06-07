package ru.md.msc.rest.event.model.request

data class GetUserEventsRequest(
	val authId: Long = 0,
	val userId: Long = 0,
)