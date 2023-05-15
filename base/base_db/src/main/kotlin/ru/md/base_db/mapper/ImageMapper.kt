package ru.md.base_db.mapper

import ru.md.base_domain.image.model.BaseImage
import ru.md.base_domain.image.model.IBaseImage

fun IBaseImage.toImage() = BaseImage(
	id = id,
	imageUrl = imageUrl,
	imageKey = imageKey,
	type = type,
	main = main,
	createdAt = createdAt
)