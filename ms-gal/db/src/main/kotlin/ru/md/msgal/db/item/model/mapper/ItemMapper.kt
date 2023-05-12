package ru.md.msgal.db.item.model.mapper

import ru.md.msgal.db.item.model.ItemEntity
import ru.md.msgal.domain.item.model.Item

fun ItemEntity.toItem() = Item(
	id = id ?: 0,
	folderId = folderId ?: 0,
	name = name ?: "",
	description = description,
	createdAt = createdAt,
	updatedAt = updatedAt
)

fun Item.toItemEntity() = ItemEntity(
	id = id,
	folderId = folderId,
	name = name,
	description = description,
	createdAt = createdAt,
	updatedAt = updatedAt,
	imageUrl = imageUrl,
	imageKey = imageKey
)