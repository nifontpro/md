package ru.md.shop.rest.product.model.response

import ru.md.base_rest.model.response.BaseImageResponse
import ru.md.shop.domain.product.model.Product

data class ProductDetailsResponse(
	val product: Product = Product(),
	val place: String? = null,
	val siteUrl: String? = null,
	val createdAt: Long = 0,
	val images: List<BaseImageResponse> = emptyList(),
)
