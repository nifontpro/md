package ru.md.shop.domain.pay.biz.proc

import ru.md.base_domain.biz.proc.BaseMedalsContext
import ru.md.base_domain.biz.proc.IBaseCommand
import ru.md.base_domain.pay.model.UserPay
import ru.md.shop.domain.pay.service.UserPayService

class PayContext : BaseMedalsContext() {
	var userPay: UserPay = UserPay()

	lateinit var userPayService: UserPayService
	//	lateinit var baseUserPayService: BaseUserPayService
	//	lateinit var productService: ProductService
}

enum class PayCommand : IBaseCommand {
	GET_USER_PAY,
}
