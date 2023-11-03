package ru.md.shop.db.product.model.mappers

import ru.md.base_db.image.mappers.toBaseImage
import ru.md.shop.db.product.model.ProductDetailsEntity
import ru.md.shop.domain.product.model.ProductDetails

fun ProductDetails.toProductDetailsEntity() = ProductDetailsEntity(
	productEntity = product.toProductEntity(),
	place = place,
	siteUrl = siteUrl,
	createdAt = createdAt
)

fun ProductDetailsEntity.toProductDetails() = ProductDetails(
	product = productEntity.toProduct(),
	place = place,
	siteUrl = siteUrl,
	createdAt = createdAt,
	images = images.map { it.toBaseImage() }
)