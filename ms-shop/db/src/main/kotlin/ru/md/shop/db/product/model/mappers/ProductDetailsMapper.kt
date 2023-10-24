package ru.md.shop.db.product.model.mappers

import ru.md.base_db.mapper.toBaseImage
import ru.md.shop.db.product.model.ProductDetailsEntity
import ru.md.shop.domain.product.model.Product
import ru.md.shop.domain.product.model.ProductDetails

fun ProductDetails.toProductDetailsEntity() = ProductDetailsEntity(
	productEntity = product.toProductEntity(),
	description = description,
	siteUrl = siteUrl,
	createdAt = createdAt
)

fun ProductDetailsEntity.toProductDetails() = ProductDetails(
	product = productEntity?.toProduct() ?: Product(),
	description = description,
	siteUrl = siteUrl,
	createdAt = createdAt,
	images = images.map { it.toBaseImage() }
)