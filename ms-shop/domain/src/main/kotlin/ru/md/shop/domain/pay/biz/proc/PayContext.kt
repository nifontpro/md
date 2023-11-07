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

	var payDataId: Long = 0

	// filters:
	var payCode: PayCode = PayCode.UNDEF
	var isActive: Boolean? = null

	lateinit var payService: PayService
	//	lateinit var baseUserPayService: BaseUserPayService
	//	lateinit var productService: ProductService
}

enum class PayCommand : IBaseCommand {
	GET_USER_PAY,
	PAY_PRODUCT,
	GET_PAYS_DATA,
	ADMIN_GIVE_PRODUCT,
	ADMIN_RETURN_PRODUCT,
	USER_RETURN_PRODUCT,
}
