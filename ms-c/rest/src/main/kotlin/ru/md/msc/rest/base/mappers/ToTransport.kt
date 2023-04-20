package ru.md.msc.rest.base.mappers

import ru.md.msc.domain.base.biz.BaseContext
import ru.md.msc.domain.image.model.BaseImage
import ru.md.msc.rest.base.BaseResponse
import ru.md.msc.rest.base.baseResponse

fun BaseContext.toTransportGetBaseImage(): BaseResponse<BaseImage> {
	return baseResponse(baseImage)
}