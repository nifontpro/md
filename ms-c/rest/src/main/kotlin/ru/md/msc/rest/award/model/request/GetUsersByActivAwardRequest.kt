package ru.md.msc.rest.award.model.request

import ru.md.msc.domain.base.model.BaseOrder

data class GetUsersByActivAwardRequest(
	val authId: Long = 0,
	val awardId: Long = 0,
	val orders: List<BaseOrder> = emptyList(),
)