package ru.md.msc.rest.award.model.request

data class DeleteAwardImageRequest(
	val authId: Long = 0,
	val awardId: Long = 0,
	val imageId: Long = 0,
)