package ru.md.shop.db.pay.model.mappers

import ru.md.base_db.user.model.mappers.toUserEntity
import ru.md.base_db.user.model.mappers.toUserLazy
import ru.md.base_db.user.model.mappers.toUserWithDeptOnly
import ru.md.base_domain.user.model.User
import ru.md.shop.db.pay.model.PayDataEntity
import ru.md.shop.db.product.model.mappers.toProduct
import ru.md.shop.db.product.model.mappers.toProductEntity
import ru.md.shop.domain.pay.model.PayData
import ru.md.shop.domain.product.model.Product

fun PayData.toPayDataEntity() = PayDataEntity(
	id = if (id == 0L) null else id,
	dateOp = dateOp,
	userEntity = user.toUserEntity(),
	productEntity = product.toProductEntity(),
	price = price,
	payCode = payCode,
	isActive = isActive,
)

fun PayDataEntity.toPayData() = PayData(
	id = id ?: 0,
	user = userEntity?.toUserLazy() ?: User(),
	product = productEntity?.toProduct() ?: Product(),
	dateOp = dateOp,
	price = price,
	payCode = payCode,
	isActive = isActive,
)

fun PayDataEntity.toPayDataWithUserDept() = PayData(
	id = id ?: 0,
	user = userEntity?.toUserWithDeptOnly() ?: User(),
	product = productEntity?.toProduct() ?: Product(),
	dateOp = dateOp,
	price = price,
	payCode = payCode,
	isActive = isActive,
)

fun PayDataEntity.toPayDataOnlyProduct() = PayData(
	id = id ?: 0,
	product = productEntity?.toProduct() ?: Product(),
	dateOp = dateOp,
	price = price,
	payCode = payCode,
	isActive = isActive,
)