package ru.md.msc.rest.user.model.response

data class UserDetailsResponse(
	val user: UserResponse? = null,
	val phone: String? = null,
	val address: String? = null,
	val description: String? = null,
	val createdAt: Long? = null,
)