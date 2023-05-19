package ru.md.msgal.rest.item.mappers

import ru.md.base_domain.item.SmallItem
import ru.md.base_domain.item.mappers.toSmallItem
import ru.md.base_domain.rest.BaseResponse
import ru.md.base_domain.rest.baseResponse
import ru.md.msgal.domain.item.biz.proc.ItemContext
import ru.md.msgal.rest.item.model.response.ItemResponse

fun ItemContext.toTransportSmallItem(): BaseResponse<SmallItem> {
	return baseResponse(item.toSmallItem())
}

fun ItemContext.toTransportItems(): BaseResponse<List<ItemResponse>> {
	return baseResponse(items.map { it.toItemResponse() })
}

