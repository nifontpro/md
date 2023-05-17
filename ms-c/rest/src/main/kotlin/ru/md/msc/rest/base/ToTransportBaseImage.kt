package ru.md.msc.rest.base

import ru.md.base_domain.image.model.BaseImage
import ru.md.base_rest.model.BaseResponse
import ru.md.base_rest.model.baseResponse
import ru.md.msc.domain.base.biz.BaseClientContext

fun BaseClientContext.toTransportBaseImage(): BaseResponse<BaseImage> {
	return baseResponse(baseImage)
}