package ru.md.msc.domain.base.model

import java.time.LocalDateTime

data class BaseQuery(
	val page: Int? = null,
	val pageSize: Int? = null,
	val filter: String? = null,
	val minDate: LocalDateTime? = null,
	val maxDate: LocalDateTime? = null,
	val orders: List<BaseOrder> = emptyList(),
)