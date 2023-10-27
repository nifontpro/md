package ru.md.base_rest.model.mapper

import ru.md.base_domain.image.model.BaseImage
import ru.md.base_domain.model.mappers.toEpochMilliUTC
import ru.md.base_rest.model.response.BaseImageResponse

fun BaseImage.toBaseImageResponse() = BaseImageResponse(
	id = id,
	originUrl = originUrl,
	originKey = originKey,
	imageUrl = normalUrl,
	imageKey = normalKey,
	miniUrl = miniUrl,
	miniKey = miniKey,
	type = type,
	main = main,
	createdAt = createdAt?.toEpochMilliUTC()
)