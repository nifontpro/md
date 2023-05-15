package ru.md.msgal.domain.base.biz

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import ru.md.base_domain.biz.proc.BaseContext

interface IBaseCommand

abstract class BaseGalleryContext(

//	var baseImage: BaseImage = BaseImage(),
//	var baseImages: List<BaseImage> = emptyList(),
//	var deleteImageOnFailing: Boolean = false,

) : BaseContext() {

	val log: Logger = LoggerFactory.getLogger(BaseGalleryContext::class.java)
}