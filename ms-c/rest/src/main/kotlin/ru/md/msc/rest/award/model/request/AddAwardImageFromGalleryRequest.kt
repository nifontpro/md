package ru.md.msc.rest.award.model.request

data class AddAwardImageFromGalleryRequest(
	val authId: Long = 0,
	val awardId: Long = 0,
	val itemId: Long = 0,
)