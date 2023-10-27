package ru.md.base_db.pay.model.mappers

import ru.md.base_db.pay.model.UserPayEntity
import ru.md.base_domain.pay.model.UserPay

fun UserPayEntity.toUserPay() = UserPay(
	id = id ?: 0,
	userId = userId,
	balance = balance,
	createdAt = createdAt
)