package ru.md.msgal.domain.folder.biz.proc

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import ru.md.base_domain.biz.proc.IBaseCommand
import ru.md.msgal.domain.base.biz.BaseGalleryContext
import ru.md.msgal.domain.folder.model.Folder

class FolderContext : BaseGalleryContext() {
	var folder: Folder = Folder()
	var folders: List<Folder> = emptyList()

	override val log: Logger = LoggerFactory.getLogger(FolderContext::class.java)
}

enum class FolderCommand : IBaseCommand {
	CREATE,
	DELETE,
	UPDATE,
	GET_ALL,
}
