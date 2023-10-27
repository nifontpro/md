package ru.md.base_domain.pay.model

import java.time.LocalDateTime

data class UserPay(
	var id: Long = 0,
	var userId: Long = 0,
	var balance: Int = 0,
	var createdAt: LocalDateTime = LocalDateTime.now(),
)