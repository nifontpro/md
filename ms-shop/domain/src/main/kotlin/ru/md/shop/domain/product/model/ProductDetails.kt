package ru.md.shop.domain.product.model

import ru.md.base_domain.image.model.BaseImage
import java.time.LocalDateTime

data class ProductDetails(
	val product: Product = Product(),
	val description: String? = null,
	val place: String? = null,
	val siteUrl: String? = null,
	val createdAt: LocalDateTime = LocalDateTime.now(),
	val images: List<BaseImage> = emptyList(),
)
