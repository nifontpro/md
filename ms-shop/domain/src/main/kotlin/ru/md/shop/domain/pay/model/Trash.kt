package ru.md.shop.domain.pay.model

import java.time.LocalDateTime

data class Trash(
	val id: Long = 0,
	val userId: Long = 0,
	val productId: Long = 0,
	val addAt: LocalDateTime = LocalDateTime.now(),
	val quantity: Int = 0,
)