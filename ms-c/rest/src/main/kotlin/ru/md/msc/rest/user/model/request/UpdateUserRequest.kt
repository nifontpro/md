package ru.md.msc.rest.user.model.request

import ru.md.msc.domain.user.model.Gender

data class UpdateUserRequest(
	val authId: Long = 0,
	val userId: Long = 0,
	val deptId: Long = 0,
	val authEmail: String? = null,
	val firstname: String = "",
	val lastname: String? = null,
	val patronymic: String? = null,
	val post: String? = null,
	val gender: Gender = Gender.UNDEF,
	val phone: String? = null,
	val address: String? = null,
	val description: String? = null,
//	val roles: Set<RoleUser> = emptySet()
)
