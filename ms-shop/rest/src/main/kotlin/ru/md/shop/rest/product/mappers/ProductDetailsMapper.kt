package ru.md.shop.rest.product.mappers

import ru.md.base_domain.model.converter.toEpochMilliUTC
import ru.md.base_rest.model.mapper.toBaseImageResponse
import ru.md.shop.domain.product.model.ProductDetails
import ru.md.shop.rest.product.model.response.ProductDetailsResponse

fun ProductDetails.toProductDetailsResponse() = ProductDetailsResponse(
	product = product,
	description = description,
	siteUrl = siteUrl,
	createdAt = createdAt.toEpochMilliUTC(),
	images = images.map { it.toBaseImageResponse() }
)