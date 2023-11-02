package ru.md.shop.rest.pay.model.request

data class PayProductRequest(
	val authId: Long = 0,
	val productId: Long = 0,
)
