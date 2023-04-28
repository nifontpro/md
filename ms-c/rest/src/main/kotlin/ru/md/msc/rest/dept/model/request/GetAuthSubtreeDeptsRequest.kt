package ru.md.msc.rest.dept.model.request

import ru.md.msc.domain.base.model.BaseOrder

data class GetAuthSubtreeDeptsRequest(
	val authId: Long = 0,
	val orders: List<BaseOrder> = emptyList()
)