package ru.md.msc.rest.user.model.request

data class DeleteUserRequest (
	val authId: Long = 0,
	val userId: Long = 0,
	val forever: Boolean = true
)