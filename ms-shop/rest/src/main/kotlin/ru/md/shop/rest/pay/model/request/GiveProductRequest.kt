package ru.md.shop.rest.pay.model.request

data class GiveProductRequest(
	val authId: Long = 0,
	val payId: Long = 0,
)
