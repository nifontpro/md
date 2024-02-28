package ru.md.msgal.domain.base.biz

import ru.md.base_domain.biz.proc.BaseMedalsContext
import ru.md.msgal.domain.folder.service.FolderService

abstract class BaseGalleryContext(

	var folderId: Long = 0,

) : BaseMedalsContext() {
	lateinit var folderService: FolderService
}