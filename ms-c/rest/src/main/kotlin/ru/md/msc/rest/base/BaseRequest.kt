package ru.md.msc.rest.base

import ru.md.msc.domain.base.model.BaseOrder

data class BaseRequest(
	val page: Int? = null,
	val pageSize: Int? = null,
	val filter: String? = null,
	val minDate: Long? = null,
	val maxDate: Long? = null,
	val orders: List<BaseOrder> = emptyList(),
)