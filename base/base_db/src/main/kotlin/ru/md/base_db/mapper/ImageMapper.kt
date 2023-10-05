package ru.md.base_db.mapper

import ru.md.base_domain.image.model.BaseImage
import ru.md.base_domain.image.model.IBaseImage

fun IBaseImage.toBaseImage() = BaseImage(
	id = id,
	originUrl = originUrl,
	originKey = originKey,
	normalUrl = normalUrl,
	normalKey = normalKey,
	miniUrl = miniUrl,
	miniKey = miniKey,
	type = type,
	main = main,
	createdAt = createdAt
)