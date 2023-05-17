package ru.md.msgal.rest.item.mappers

import ru.md.base_rest.model.BaseResponse
import ru.md.base_rest.model.baseResponse
import ru.md.msgal.domain.item.biz.proc.ItemContext
import ru.md.msgal.rest.item.model.response.ItemResponse

fun ItemContext.toTransportItem(): BaseResponse<ItemResponse> {
	return baseResponse(item.toItemResponse())
}

fun ItemContext.toTransportItems(): BaseResponse<List<ItemResponse>> {
	return baseResponse(items.map { it.toItemResponse() })
}

