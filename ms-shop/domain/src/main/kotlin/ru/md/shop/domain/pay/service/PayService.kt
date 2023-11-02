package ru.md.shop.domain.pay.service

import ru.md.base_domain.pay.model.UserPay
import ru.md.shop.domain.pay.model.PayData

interface PayService {
	fun getUserPayData(userId: Long): UserPay
	fun payProduct(productId: Long, userId: Long): PayData
}