package ru.md.msc.domain.base.model

import java.time.LocalDateTime

data class BaseQuery(
	val minDate: LocalDateTime? = null,
	val maxDate: LocalDateTime? = null,
)