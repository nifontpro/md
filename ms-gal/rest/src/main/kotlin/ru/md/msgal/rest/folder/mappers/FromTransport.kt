package ru.md.msgal.rest.folder.mappers

import ru.md.base_rest.model.mapper.toBaseQuery
import ru.md.msgal.domain.folder.biz.proc.FolderCommand
import ru.md.msgal.domain.folder.biz.proc.FolderContext
import ru.md.msgal.domain.folder.model.Folder
import ru.md.msgal.rest.folder.model.request.CreateFolderRequest
import ru.md.msgal.rest.folder.model.request.DeleteFolderRequest
import ru.md.msgal.rest.folder.model.request.GetAllFolderRequest
import ru.md.msgal.rest.folder.model.request.UpdateFolderRequest

fun FolderContext.fromTransport(request: CreateFolderRequest) {
	command = FolderCommand.CREATE

	folder = Folder(
		parentId = request.parentId,
		name = request.name,
		description = request.description
	)
}

fun FolderContext.fromTransport(request: UpdateFolderRequest) {
	command = FolderCommand.UPDATE
	folderId = request.folderId
	folder = Folder(
		id = folderId,
		name = request.name,
		description = request.description
	)
}

fun FolderContext.fromTransport(request: DeleteFolderRequest) {
	command = FolderCommand.DELETE
	folderId = request.folderId
}

fun FolderContext.fromTransport(request: GetAllFolderRequest) {
	command = FolderCommand.GET_ALL
	baseQuery = request.baseRequest.toBaseQuery()
}

