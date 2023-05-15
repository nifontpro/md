package ru.md.msc.rest.base.mappers

import ru.md.base_domain.image.model.BaseImage
import ru.md.msc.domain.base.biz.BaseClientContext
import ru.md.base_rest.model.BaseResponse
import ru.md.base_rest.model.baseResponse

fun BaseClientContext.toTransportBaseImage(): BaseResponse<BaseImage> {
	return baseResponse(baseImage)
}