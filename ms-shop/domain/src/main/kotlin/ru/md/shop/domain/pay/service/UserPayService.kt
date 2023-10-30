package ru.md.shop.domain.pay.service

import ru.md.base_domain.pay.model.UserPay

interface UserPayService {
	fun getPayData(userId: Long): UserPay
}