package ru.md.shop.rest.product.mappers

import ru.md.base_domain.model.mappers.toEpochMilliUTC
import ru.md.base_rest.model.mapper.toBaseImageResponse
import ru.md.shop.domain.product.model.ProductDetails
import ru.md.shop.rest.product.model.response.ProductDetailsResponse

fun ProductDetails.toProductDetailsResponse() = ProductDetailsResponse(
	product = product,
	description = description,
	place = place,
	siteUrl = siteUrl,
	createdAt = createdAt.toEpochMilliUTC(),
	images = images.map { it.toBaseImageResponse() },
	secondImages = secondImages.map { it.toBaseImageResponse() }
)