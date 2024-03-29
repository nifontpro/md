package ru.md.base_rest.model.request

import ru.md.base_domain.model.BaseOrder

data class BaseRequest(
	val page: Int? = null,
	val pageSize: Int? = null,
	val filter: String? = null,
	val minDate: Long? = null,
	val maxDate: Long? = null,
	val subdepts: Boolean = false,
	val orders: List<BaseOrder> = emptyList(),
)