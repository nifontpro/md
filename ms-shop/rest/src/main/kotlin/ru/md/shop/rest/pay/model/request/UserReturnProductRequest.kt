package ru.md.shop.rest.pay.model.request

data class UserReturnProductRequest(
	val authId: Long = 0,
	val payDataId: Long = 0,
)
