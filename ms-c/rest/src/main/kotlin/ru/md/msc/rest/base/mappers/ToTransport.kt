package ru.md.msc.rest.base.mappers

import ru.md.base_domain.image.model.BaseImage
import ru.md.msc.domain.base.biz.BaseClientContext
import ru.md.msc.rest.base.BaseResponse
import ru.md.msc.rest.base.baseResponse

fun BaseClientContext.toTransportBaseImage(): BaseResponse<BaseImage> {
	return baseResponse(baseImage)
}