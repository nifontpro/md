package ru.md.msc.rest.base.mappers

import ru.md.base_domain.image.model.BaseImage
import ru.md.base_domain.rest.BaseResponse
import ru.md.base_domain.rest.baseResponse
import ru.md.base_rest.model.mapper.toBaseImageResponse
import ru.md.base_rest.model.response.BaseImageResponse
import ru.md.msc.domain.base.biz.BaseClientContext

fun BaseClientContext.toTransportBaseImage(): BaseResponse<BaseImage> {
	return baseResponse(baseImage)
}

fun BaseClientContext.toTransportBaseImageResponse(): BaseResponse<BaseImageResponse> {
	return baseResponse(baseImage.toBaseImageResponse())
}


fun BaseClientContext.toTransportUnit(): BaseResponse<Unit> {
	return baseResponse(Unit)
}

//fun BaseClientContext.toTransportDeptId(): BaseResponse<Long> {
//	return baseResponse(deptId)
//}