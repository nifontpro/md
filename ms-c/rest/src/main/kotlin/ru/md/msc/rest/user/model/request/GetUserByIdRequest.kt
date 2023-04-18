package ru.md.msc.rest.user.model.request

data class GetUserByIdRequest (
	val authId: Long = 0,
	val userId: Long = 0
)