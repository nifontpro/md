package ru.md.award.rest.medal.model.request

data class UpdateMedalRequest(
	val authId: Long = 0,
	val medalId: Long = 0,

	val name: String = "",
	val score: Int = 1,
	val description: String? = null,
)