package ru.md.msgal.db.item.model.mapper

import ru.md.msgal.db.item.model.ItemEntity
import ru.md.base_domain.item.GalleryItem
import java.time.LocalDateTime

fun ItemEntity.toItem() = GalleryItem(
	id = id ?: 0,
	folderId = folderId ?: 0,
	name = name ?: "",
	description = description,
	createdAt = createdAt,
	updatedAt = updatedAt,
	imageUrl = imageUrl ?: "",
	imageKey = imageKey ?: ""
)

fun GalleryItem.toItemEntity(create: Boolean = false) = ItemEntity(
	id = if (create) null else id,
	folderId = folderId,
	name = name,
	description = description,
	createdAt = if (create) LocalDateTime.now() else createdAt,
	updatedAt = updatedAt,
	imageUrl = imageUrl,
	imageKey = imageKey
)