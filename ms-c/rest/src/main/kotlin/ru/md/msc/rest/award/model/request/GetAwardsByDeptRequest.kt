package ru.md.msc.rest.award.model.request

import ru.md.msc.domain.base.model.BaseOrder

data class GetAwardsByDeptRequest(
	val authId: Long = 0,
	val deptId: Long = 0,
	val orders: List<BaseOrder> = emptyList()
)