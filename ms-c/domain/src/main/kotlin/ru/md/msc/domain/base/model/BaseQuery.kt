package ru.md.msc.domain.base.model

import ru.md.msc.domain.base.biz.DEFAULT_PAGE_SIZE
import java.time.LocalDateTime

data class BaseQuery(
	val page: Int = 0,
	val pageSize: Int = DEFAULT_PAGE_SIZE,
	val filter: String? = null,
	val minDate: LocalDateTime? = null,
	val maxDate: LocalDateTime? = null,
	val orders: List<BaseOrder> = emptyList(),
)