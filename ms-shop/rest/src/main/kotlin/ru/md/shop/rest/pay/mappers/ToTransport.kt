package ru.md.shop.rest.pay.mappers

import ru.md.base_domain.model.BaseResponse
import ru.md.base_domain.model.baseResponse
import ru.md.shop.domain.pay.biz.proc.PayContext
import ru.md.shop.rest.pay.model.response.PayDataResponse
import ru.md.shop.rest.pay.model.response.UserPayResponse

fun PayContext.toTransportUserPayResponse(): BaseResponse<UserPayResponse> {
	return baseResponse(userPay.toUserPayResponse())
}

fun PayContext.toTransportPayDataResponse(): BaseResponse<PayDataResponse> {
	return baseResponse(payData.toPayDataResponse())
}

fun PayContext.toTransportPaysDataResponse(): BaseResponse<List<PayDataResponse>> {
	return baseResponse(paysData.map { it.toPayDataResponse() })
}
