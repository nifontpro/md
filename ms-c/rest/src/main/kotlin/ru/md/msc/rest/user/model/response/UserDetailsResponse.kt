package ru.md.msc.rest.user.model.response

import ru.md.msc.domain.user.model.User

data class UserDetailsResponse(
	val user: User? = null,
	val phone: String? = null,
	val address: String? = null,
	val description: String? = null,
	val createdAt: Long? = null,
)