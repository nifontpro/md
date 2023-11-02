package ru.md.shop.rest.pay.mappers

import ru.md.base_rest.model.mapper.toBaseQuery
import ru.md.shop.domain.pay.biz.proc.PayCommand
import ru.md.shop.domain.pay.biz.proc.PayContext
import ru.md.shop.rest.pay.model.request.GetPaysDataRequest
import ru.md.shop.rest.pay.model.request.PayProductRequest
import ru.md.shop.rest.pay.model.request.GetUserPayRequest

fun PayContext.fromTransport(request: GetUserPayRequest) {
	command = PayCommand.GET_USER_PAY
	authId = request.authId
	userId = request.userId
}

fun PayContext.fromTransport(request: PayProductRequest) {
	command = PayCommand.PAY_PRODUCT
	authId = request.authId
	productId = request.productId
}

fun PayContext.fromTransport(request: GetPaysDataRequest) {
	command = PayCommand.GET_PAYS_DATA
	authId = request.authId
	userId = request.userId ?: run {
		userIdNotPresent = true
		0
	}
	deptId = request.deptId
	payCode = request.payCode
	isActive = request.isActive
	baseQuery = request.baseRequest.toBaseQuery()
}
