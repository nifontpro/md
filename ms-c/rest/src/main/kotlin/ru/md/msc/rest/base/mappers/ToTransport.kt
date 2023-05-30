package ru.md.msc.rest.base.mappers

import ru.md.base_domain.image.model.BaseImage
import ru.md.base_domain.rest.BaseResponse
import ru.md.base_domain.rest.baseResponse
import ru.md.msc.domain.base.biz.BaseClientContext

fun BaseClientContext.toTransportBaseImage(): BaseResponse<BaseImage> {
	return baseResponse(baseImage)
}

fun BaseClientContext.toTransportUnit(): BaseResponse<Unit> {
	return baseResponse(Unit)
}
