package ru.md.shop.domain.pay.service

import ru.md.base_domain.model.BaseQuery
import ru.md.base_domain.model.PageResult
import ru.md.base_domain.pay.model.UserPay
import ru.md.shop.domain.pay.model.PayCode
import ru.md.shop.domain.pay.model.PayData

interface PayService {
	fun getUserPayData(userId: Long): UserPay
	fun payProduct(productId: Long, userId: Long): PayData
	fun getPaysData(
		deptId: Long,
		userId: Long,
		baseQuery: BaseQuery = BaseQuery(),
		payCode: PayCode = PayCode.UNDEF,
		isActive: Boolean? = null
	): PageResult<PayData>

	fun findById(payDataId: Long): PayData
	fun addOperation(payData: PayData, payCode: PayCode): PayData
}