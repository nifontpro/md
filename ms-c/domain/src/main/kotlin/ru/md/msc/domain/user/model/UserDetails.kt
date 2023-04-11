package ru.md.msc.domain.user.model

import java.time.LocalDateTime

data class UserDetails(
	val user: User? = null,
	val phone: String? = null,
	val address: String? = null,
	val description: String? = null,
	val createdAt: LocalDateTime? = null,
)