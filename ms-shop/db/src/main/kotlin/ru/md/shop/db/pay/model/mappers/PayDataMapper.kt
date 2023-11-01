package ru.md.shop.db.pay.model.mappers

import ru.md.base_db.user.model.mappers.toUserLazy
import ru.md.base_domain.user.model.User
import ru.md.shop.db.pay.model.PayDataEntity
import ru.md.shop.db.product.model.mappers.toProduct
import ru.md.shop.domain.pay.model.PayData
import ru.md.shop.domain.product.model.Product

fun PayDataEntity.toPayData() = PayData(
	id = id ?: 0,
	dateOp = dateOp,
	user = userEntity?.toUserLazy() ?: User(),
	product = productEntity?.toProduct() ?: Product()
)