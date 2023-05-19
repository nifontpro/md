package ru.md.msgal.rest.item.mappers

import ru.md.base_domain.model.converter.toEpochMilliUTC
import ru.md.base_domain.item.GalleryItem
import ru.md.msgal.rest.item.model.response.ItemResponse

fun GalleryItem.toItemResponse() = ItemResponse(
	id = id,
	folderId = folderId,
	name = name,
	description = description,
	imageUrl = imageUrl,
	imageKey = imageKey,
	createdAt = createdAt?.toEpochMilliUTC(),
	updatedAt = updatedAt?.toEpochMilliUTC(),
)