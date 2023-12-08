package ru.md.shop.rest.pay.mappers

import ru.md.base_rest.model.mapper.toBaseQuery
import ru.md.shop.domain.pay.biz.proc.PayCommand
import ru.md.shop.domain.pay.biz.proc.PayContext
import ru.md.shop.rest.pay.model.request.*

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
	userId = request.userId
	deptId = request.deptId
	payCode = request.payCode
	isActive = request.isActive
	baseQuery = request.baseRequest.toBaseQuery()
}

fun PayContext.fromTransport(request: GiveProductRequest) {
	command = PayCommand.ADMIN_GIVE_PRODUCT
	authId = request.authId
	payDataId = request.payDataId
}

fun PayContext.fromTransport(request: AdminReturnProductRequest) {
	command = PayCommand.ADMIN_RETURN_PRODUCT
	authId = request.authId
	payDataId = request.payDataId
}

fun PayContext.fromTransport(request: UserReturnProductRequest) {
	command = PayCommand.USER_RETURN_PRODUCT
	authId = request.authId
	payDataId = request.payDataId
}

fun PayContext.fromTransport(request: ReturnProductRequest) {
	command = PayCommand.RETURN_PRODUCT
	authId = request.authId
	payDataId = request.payDataId
}

