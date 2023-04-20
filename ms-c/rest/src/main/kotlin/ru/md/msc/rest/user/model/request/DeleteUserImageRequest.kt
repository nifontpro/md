package ru.md.msc.rest.user.model.request

data class DeleteUserImageRequest(
	val userId: Long = 0,
	val imageId: Long = 0,
)