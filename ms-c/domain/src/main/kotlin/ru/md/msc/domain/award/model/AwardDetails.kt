package ru.md.msc.domain.award.model

import java.time.LocalDateTime

data class AwardDetails(
	val award: Award = Award(),
	val description: String? = null,
	val criteria: String? = null,
	val createdAt: LocalDateTime? = null,
)