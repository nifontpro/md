package ru.md.base.base_db.mapper

import ru.md.base.domain.image.model.IBaseImage
import ru.md.base.domain.image.model.BaseImage

fun IBaseImage.toImage() = BaseImage(
	id = id,
	imageUrl = imageUrl,
	imageKey = imageKey,
	type = type,
	main = main,
	createdAt = createdAt
)