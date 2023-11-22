package ru.md.award.rest.medal.model.request

data class DeleteMedalImageRequest(
	val authId: Long = 0,
	val medalId: Long = 0,
	val imageId: Long = 0,
)