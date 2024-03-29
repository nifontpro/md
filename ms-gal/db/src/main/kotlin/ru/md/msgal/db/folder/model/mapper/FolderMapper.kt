package ru.md.msgal.db.folder.model.mapper

import ru.md.msgal.db.folder.model.FolderEntity
import ru.md.msgal.domain.folder.model.Folder
import java.time.LocalDateTime

fun FolderEntity.toFolder() = Folder(
	id = id ?: 0,
	parentId = parentId ?: 0,
	name = name ?: "",
	description = description,
	createdAt = createdAt,
	updatedAt = updatedAt
)

fun Folder.toFolderEntity(create: Boolean = false) = FolderEntity(
	id = if (create) null else id,
	parentId = parentId,
	name = name,
	description = description,
	createdAt = if (create) LocalDateTime.now() else createdAt,
	updatedAt = if (create) null else updatedAt
)