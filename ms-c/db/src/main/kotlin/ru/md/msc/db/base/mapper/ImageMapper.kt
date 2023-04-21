package ru.md.msc.db.base.mapper

import ru.md.msc.domain.image.model.IBaseImage
import ru.md.msc.domain.image.model.BaseImage

fun IBaseImage.toImage() = BaseImage(
	id = id,
	imageUrl = imageUrl,
//	imageKey = imageKey,
	type = type,
	main = main,
	createdAt = createdAt
)