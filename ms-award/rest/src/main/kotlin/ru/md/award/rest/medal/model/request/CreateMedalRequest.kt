package ru.md.award.rest.medal.model.request

data class CreateMedalRequest(
	val authId: Long = 0,
	val deptId: Long = 0,

	val name: String = "",
	val score: Int = 1,
	val description: String? = null,
)