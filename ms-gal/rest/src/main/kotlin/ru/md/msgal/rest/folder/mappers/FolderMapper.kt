package ru.md.msgal.rest.folder.mappers

import ru.md.base_domain.model.converter.toEpochMilliUTC
import ru.md.msgal.domain.folder.model.Folder
import ru.md.msgal.rest.folder.model.response.FolderResponse

fun Folder.toFolderResponse() = FolderResponse(
	id = id,
	parentId = parentId,
	name = name,
	description = description,
	createdAt = createdAt?.toEpochMilliUTC(),
	updatedAt = updatedAt?.toEpochMilliUTC(),
)