package ru.md.base_domain.gallery.mappers

import ru.md.base_domain.gallery.GalleryItem
import ru.md.base_domain.gallery.SmallItem

fun GalleryItem.toSmallItem() = SmallItem(
	id = id,
	folderId = folderId,
	name = name,
	originUrl = originUrl,
	originKey = originKey,
	normalUrl = normalUrl,
	normalKey = normalKey,
	miniUrl = miniUrl,
	miniKey = miniKey
)