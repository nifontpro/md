package ru.md.base_rest.model.mapper

import ru.md.base_domain.biz.proc.BaseContext
import ru.md.base_domain.image.model.BaseImage
import ru.md.base_rest.model.BaseResponse
import ru.md.base_rest.model.baseResponse

fun BaseContext.toTransportBaseImage(): BaseResponse<BaseImage> {
	return baseResponse(baseImage)
}