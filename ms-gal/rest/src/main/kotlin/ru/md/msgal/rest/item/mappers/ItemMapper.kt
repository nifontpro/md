package ru.md.msgal.rest.item.mappers

import ru.md.base_domain.gallery.GalleryItem
import ru.md.base_domain.model.converter.toEpochMilliUTC
import ru.md.msgal.rest.item.model.response.ItemResponse

fun GalleryItem.toItemResponse() = ItemResponse(
	id = id,
	folderId = folderId,
	name = name,
	description = description,
	originUrl = originUrl,
	originKey = originKey,
	imageUrl = normalUrl,
	imageKey = normalKey,
	miniUrl = miniUrl,
	miniKey = miniKey,
	createdAt = createdAt?.toEpochMilliUTC(),
	updatedAt = updatedAt?.toEpochMilliUTC(),
)