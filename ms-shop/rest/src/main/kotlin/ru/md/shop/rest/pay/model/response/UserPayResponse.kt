package ru.md.shop.rest.pay.model.response

data class UserPayResponse(
	val id: Long,
	val userId: Long,
	val balance: Int,
	val createdAt: Long,
)