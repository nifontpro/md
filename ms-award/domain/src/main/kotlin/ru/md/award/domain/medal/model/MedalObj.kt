package ru.md.award.domain.medal.model

data class MedalObj(
	val id: Long = 0,
	val actId: Long = 0,
	val count: Int = 0,
	val score: Int = 0,
	val medal: Medal,
)