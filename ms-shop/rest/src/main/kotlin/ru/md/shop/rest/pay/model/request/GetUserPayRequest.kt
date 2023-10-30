package ru.md.shop.rest.pay.model.request

data class GetUserPayRequest(
	val authId: Long = 0,
	val userId: Long = 0,
)
