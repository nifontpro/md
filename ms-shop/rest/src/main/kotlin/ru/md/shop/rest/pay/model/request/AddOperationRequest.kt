package ru.md.shop.rest.pay.model.request

import ru.md.shop.domain.pay.model.PayCode

data class AddOperationRequest(
	val authId: Long = 0,
	val payId: Long = 0,
	val payCode: PayCode = PayCode.UNDEF,
)
