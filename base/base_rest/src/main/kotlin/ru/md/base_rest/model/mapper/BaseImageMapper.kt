package ru.md.base_rest.model.mapper

import ru.md.base_domain.image.model.BaseImage
import ru.md.base_domain.model.converter.toEpochMilliUTC
import ru.md.base_rest.model.response.BaseImageResponse

fun BaseImage.toBaseImageResponse() = BaseImageResponse(
	id = id,
	imageUrl = imageUrl,
	imageKey = imageKey,
	miniUrl = miniUrl,
	miniKey = miniKey,
	type = type,
	main = main,
	createdAt = createdAt?.toEpochMilliUTC()
)