package ru.md.shop.rest.pay.mappers

import ru.md.base_domain.model.mappers.toEpochMilliUTC
import ru.md.shop.domain.pay.model.PayData
import ru.md.shop.rest.pay.model.response.PayDataResponse

fun PayData.toPayDataResponse() = PayDataResponse(
	id = id,
	dateOp = dateOp.toEpochMilliUTC(),
	user = user,
	product = product,
	price = price,
	payCode = payCode,
	isActive = isActive
)