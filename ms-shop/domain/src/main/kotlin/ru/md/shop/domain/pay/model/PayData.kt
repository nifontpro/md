package ru.md.shop.domain.pay.model

import ru.md.base_domain.user.model.User
import ru.md.shop.domain.product.model.Product
import java.time.LocalDateTime

data class PayData(
	val id: Long = 0,
	val dateOp: LocalDateTime = LocalDateTime.now(),
	val user: User = User(),
	val product: Product = Product(),
	val price: Int = 0,
	val payCode: PayCode = PayCode.UNDEF,
	val isActive: Boolean = false,
)