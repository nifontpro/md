package ru.md.msgal.db.folder.model.mapper

import ru.md.msgal.db.folder.model.FolderEntity
import ru.md.msgal.domain.folder.model.Folder

fun FolderEntity.toFolder() = Folder(
	id = id ?: 0,
	parentId = parentId ?: 0,
	name = name ?: "",
	description = description,
	createdAt = createdAt,
	updatedAt = updatedAt
)

fun Folder.toFolderEntity() = FolderEntity(
	id = id,
	parentId = parentId,
	name = name,
	description = description,
	createdAt = createdAt,
	updatedAt = updatedAt
)