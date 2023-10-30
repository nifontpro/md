package ru.md.shop.rest.pay.mappers

import ru.md.base_domain.model.BaseResponse
import ru.md.base_domain.model.baseResponse
import ru.md.shop.domain.pay.biz.proc.PayContext
import ru.md.shop.rest.pay.model.response.UserPayResponse

fun PayContext.toTransportUserPayResponse(): BaseResponse<UserPayResponse> {
	return baseResponse(userPay.toUserPayResponse())
}
