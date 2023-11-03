package ru.md.shop.domain.pay.biz.proc

import ru.md.base_domain.biz.proc.IBaseCommand
import ru.md.base_domain.pay.model.UserPay
import ru.md.shop.domain.base.biz.proc.ShopContext
import ru.md.shop.domain.pay.model.PayCode
import ru.md.shop.domain.pay.model.PayData
import ru.md.shop.domain.pay.service.PayService

class PayContext : ShopContext() {
	var userPay: UserPay = UserPay()
	var payData: PayData = PayData()
	var paysData: List<PayData> = emptyList()

	// filters:
	var payCode: PayCode? = null
	var isActive: Boolean? = null
	var userIdNotPresent: Boolean = false

	lateinit var payService: PayService
	//	lateinit var baseUserPayService: BaseUserPayService
	//	lateinit var productService: ProductService
}

enum class PayCommand : IBaseCommand {
	GET_USER_PAY,
	PAY_PRODUCT,
	GET_PAYS_DATA,
}