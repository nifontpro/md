package ru.md.msc.rest.medal.model.request

data class DeleteMedalRequest(
	val authId: Long = 0,
	val medalId: Long = 0
)