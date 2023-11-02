package ru.md.shop.rest.pay.model.response

import ru.md.base_domain.user.model.User
import ru.md.shop.domain.pay.model.PayCode
import ru.md.shop.domain.product.model.Product

class PayDataResponse(
	var id: Long,
	var dateOp: Long,
	var user: User,
	var product: Product,
	var price: Int,
	var payCode: PayCode,
	var isActive: Boolean,
)