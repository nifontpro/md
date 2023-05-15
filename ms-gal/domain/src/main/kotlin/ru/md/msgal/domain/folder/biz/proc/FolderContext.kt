package ru.md.msgal.domain.folder.biz.proc

import ru.md.msgal.domain.base.biz.BaseContext
import ru.md.msgal.domain.base.biz.IBaseCommand
import ru.md.msgal.domain.folder.model.Folder
import ru.md.msgal.domain.folder.service.FolderService

class FolderContext : BaseContext() {
	var folder: Folder = Folder()
	var folders: List<Folder> = emptyList()
	var folderId: Long = 0
	lateinit var folderService: FolderService
}

enum class FolderCommand : IBaseCommand {
	CREATE,
	DELETE,
	UPDATE,
	GET_BY_ID_DETAILS,
}
