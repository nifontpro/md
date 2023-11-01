package ru.md.shop.rest.pay.mappers

import ru.md.shop.domain.pay.biz.proc.PayCommand
import ru.md.shop.domain.pay.biz.proc.PayContext
import ru.md.shop.rest.pay.model.request.BuyRequest
import ru.md.shop.rest.pay.model.request.GetUserPayRequest

fun PayContext.fromTransport(request: GetUserPayRequest) {
	command = PayCommand.GET_USER_PAY
	authId = request.authId
	userId = request.userId
}

fun PayContext.fromTransport(request: BuyRequest) {
	command = PayCommand.BUY
	authId = request.authId
	productId = request.productId
}
