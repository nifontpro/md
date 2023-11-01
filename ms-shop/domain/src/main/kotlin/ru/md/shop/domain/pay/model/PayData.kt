package ru.md.shop.domain.pay.model

import ru.md.base_domain.user.model.User
import ru.md.shop.domain.product.model.Product
import java.time.LocalDateTime

class PayData(
	var id: Long = 0,
	var dateOp: LocalDateTime = LocalDateTime.now(),
	var user: User = User(),
	var product: Product = Product(),
	var price: Int = 0,
	var payCode: PayCode = PayCode.UNDEF,
	var isActive: Boolean = false,
)