package ru.md.shop.rest.pay.model.request

data class AdminReturnProductRequest(
	val authId: Long = 0,
	val payId: Long = 0,
)
