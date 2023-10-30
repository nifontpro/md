package ru.md.shop.rest.pay.mappers

import ru.md.base_domain.model.mappers.toEpochMilliUTC
import ru.md.base_domain.pay.model.UserPay
import ru.md.shop.rest.pay.model.response.UserPayResponse

fun UserPay.toUserPayResponse() = UserPayResponse(
	id = id,
	userId = userId,
	balance = balance,
	createdAt = createdAt.toEpochMilliUTC()
)