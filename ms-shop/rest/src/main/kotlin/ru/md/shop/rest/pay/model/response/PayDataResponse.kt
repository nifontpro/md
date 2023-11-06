package ru.md.shop.rest.pay.model.response

import ru.md.base_domain.user.model.User
import ru.md.shop.domain.pay.model.PayCode
import ru.md.shop.domain.product.model.Product

data class PayDataResponse(
	val id: Long,
	val dateOp: Long,
	val user: User,
	val product: Product,
	val price: Int,
	val payCode: PayCode,
	val isActive: Boolean,
)