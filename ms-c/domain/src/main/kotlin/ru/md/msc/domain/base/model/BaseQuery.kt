package ru.md.msc.domain.base.model

import java.time.LocalDateTime

data class BaseQuery(
	val orders: List<BaseOrder> = emptyList(),
	val minDate: LocalDateTime? = null,
	val maxDate: LocalDateTime? = null,
)