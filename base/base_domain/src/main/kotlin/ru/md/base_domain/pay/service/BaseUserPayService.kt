package ru.md.base_domain.pay.service

interface BaseUserPayService {
	fun changeBalance(userId: Long, delta: Int, comment: String? = null): Int
}