package ru.md.msc.rest.user.model.request

data class DeleteUserImageRequest(
	val authId: Long = 0,
	val userId: Long = 0,
	val imageId: Long = 0,
)