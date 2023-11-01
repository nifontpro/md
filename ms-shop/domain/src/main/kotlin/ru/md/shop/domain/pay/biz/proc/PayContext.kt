package ru.md.shop.domain.pay.biz.proc

import ru.md.base_domain.biz.proc.IBaseCommand
import ru.md.base_domain.pay.model.UserPay
import ru.md.shop.domain.base.biz.proc.ShopContext
import ru.md.shop.domain.pay.service.PayService

class PayContext : ShopContext() {
	var userPay: UserPay = UserPay()

	lateinit var payService: PayService
	//	lateinit var baseUserPayService: BaseUserPayService
	//	lateinit var productService: ProductService
}

enum class PayCommand : IBaseCommand {
	GET_USER_PAY,
	BUY,
}
