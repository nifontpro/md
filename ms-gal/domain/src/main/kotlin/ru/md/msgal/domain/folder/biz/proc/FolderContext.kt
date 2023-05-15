@file:Suppress("unused")

package ru.md.msgal.domain.folder.biz.proc

import ru.md.base_domain.biz.proc.IBaseCommand
import ru.md.msgal.domain.base.biz.BaseGalleryContext
import ru.md.msgal.domain.folder.model.Folder
import ru.md.msgal.domain.folder.service.FolderService

class FolderContext : BaseGalleryContext() {
	var folder: Folder = Folder()
	var folders: List<Folder> = emptyList()
	lateinit var folderService: FolderService
}

enum class FolderCommand : IBaseCommand {
	CREATE,
	DELETE,
	UPDATE,
	GET_BY_ID_DETAILS,
}
