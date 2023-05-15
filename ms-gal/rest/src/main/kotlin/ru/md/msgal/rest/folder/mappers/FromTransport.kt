package ru.md.msgal.rest.folder.mappers

import ru.md.msgal.domain.folder.biz.proc.FolderCommand
import ru.md.msgal.domain.folder.biz.proc.FolderContext
import ru.md.msgal.domain.folder.model.Folder
import ru.md.msgal.rest.folder.model.request.CreateFolderRequest

fun FolderContext.fromTransport(request: CreateFolderRequest) {
	command = FolderCommand.CREATE

	folder = Folder(
		parentId = request.parentId,
		name = request.name,
		description = request.description
	)
}

