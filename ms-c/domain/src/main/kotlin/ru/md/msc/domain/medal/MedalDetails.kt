package ru.md.msc.domain.medal

import java.time.LocalDateTime

data class MedalDetails(
	val medalId: Long = 0,
	val description: String? = null,
	val createdAt: LocalDateTime? = null,
	val medal: Medal
)
