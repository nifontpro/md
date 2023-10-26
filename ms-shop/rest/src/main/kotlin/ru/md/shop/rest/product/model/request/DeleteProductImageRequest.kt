package ru.md.shop.rest.product.model.request

data class DeleteProductImageRequest(
	val authId: Long = 0,
	val productId: Long = 0,
	val imageId: Long = 0,
)