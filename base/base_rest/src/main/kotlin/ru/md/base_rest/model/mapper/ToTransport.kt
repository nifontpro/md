package ru.md.base_rest.model.mapper

import ru.md.base_domain.biz.proc.BaseMedalsContext
import ru.md.base_domain.model.BaseResponse
import ru.md.base_domain.model.baseResponse
import ru.md.base_rest.model.response.BaseImageResponse

fun BaseMedalsContext.toTransportBaseImageResponse(): BaseResponse<BaseImageResponse> {
	return baseResponse(baseImage.toBaseImageResponse())
}


fun BaseMedalsContext.toTransportUnit(): BaseResponse<Unit> {
	return baseResponse(Unit)
}