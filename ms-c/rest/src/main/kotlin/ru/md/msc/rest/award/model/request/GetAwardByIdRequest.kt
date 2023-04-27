package ru.md.msc.rest.award.model.request

data class GetAwardByIdRequest(
	val authId: Long = 0,
	val awardId: Long = 0
)