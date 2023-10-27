package ru.md.base_domain.pay.service

import ru.md.base_domain.pay.model.UserPay

interface BaseUserPayService {
	fun changeBalance(userId: Long, delta: Int, comment: String? = null)
	fun getPayData(userId: Long): UserPay
}