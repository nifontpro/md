package ru.md.msgal.rest.item.mappers

import ru.md.base_domain.model.converter.toEpochMilliUTC
import ru.md.msgal.domain.item.model.Item
import ru.md.msgal.rest.item.model.response.ItemResponse

fun Item.toItemResponse() = ItemResponse(
	id = id,
	folderId = folderId,
	name = name,
	description = description,
	imageUrl = imageUrl,
	imageKey = imageKey,
	createdAt = createdAt?.toEpochMilliUTC(),
	updatedAt = updatedAt?.toEpochMilliUTC(),
)