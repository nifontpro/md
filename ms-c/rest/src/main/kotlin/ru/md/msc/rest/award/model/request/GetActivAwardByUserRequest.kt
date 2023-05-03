package ru.md.msc.rest.award.model.request

data class GetActivAwardByUserRequest(
	val authId: Long = 0,
	val userId: Long = 0,
)