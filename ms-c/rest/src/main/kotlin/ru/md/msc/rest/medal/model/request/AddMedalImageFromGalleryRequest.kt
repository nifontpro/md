package ru.md.msc.rest.medal.model.request

data class AddMedalImageFromGalleryRequest(
	val authId: Long = 0,
	val medalId: Long = 0,
	val itemId: Long = 0,
)