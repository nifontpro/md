package ru.md.base_domain.item.mappers

import ru.md.base_domain.item.GalleryItem
import ru.md.base_domain.item.SmallItem

fun GalleryItem.toSmallItem() = SmallItem(
	id = id,
	folderId = folderId,
	name = name,
	imageUrl = imageUrl,
	imageKey = imageKey
)