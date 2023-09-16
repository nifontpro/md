package ru.md.msc.domain.medal.model

import java.time.LocalDateTime

data class MedalDetails(
	val medal: Medal = Medal(),
	val description: String? = null,
	val createdAt: LocalDateTime = LocalDateTime.now(),
)
