package ru.md.shop.rest.product.model.request

data class GetProductByIdRequest(
	val authId: Long = 0,
	val productId: Long = 0
)