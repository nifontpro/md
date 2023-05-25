package ru.md.msc.domain.award.model

data class AwardStateCount(
	val finish: Long = -1,
	val nominee: Long = -1,
	val future: Long = -1,
	val error: Long = -1,
)
