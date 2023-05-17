package ru.md.msgal.domain.base.biz

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import ru.md.base_domain.biz.proc.BaseContext
import ru.md.msgal.domain.folder.service.FolderService

abstract class BaseGalleryContext(

	var folderId: Long = 0,

//	var baseImage: BaseImage = BaseImage(),
//	var baseImages: List<BaseImage> = emptyList(),
//	var deleteImageOnFailing: Boolean = false,

) : BaseContext() {
	lateinit var folderService: FolderService
	open val log: Logger = LoggerFactory.getLogger(BaseGalleryContext::class.java)
}