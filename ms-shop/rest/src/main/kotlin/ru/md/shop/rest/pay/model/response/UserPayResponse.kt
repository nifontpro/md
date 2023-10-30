package ru.md.shop.rest.pay.model.response

data class UserPayResponse(
	var id: Long,
	var userId: Long,
	var balance: Int,
	var createdAt: Long,
)