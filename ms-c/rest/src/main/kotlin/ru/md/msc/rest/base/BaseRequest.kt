package ru.md.msc.rest.base

import ru.md.msc.domain.base.model.BaseOrder

data class BaseRequest(
	val orders: List<BaseOrder> = emptyList(),
	val minDate: Long? = null,
	val maxDate: Long? = null,
)