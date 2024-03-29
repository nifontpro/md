package ru.md.msc.rest.user.model.request

import ru.md.base_domain.user.model.Gender
import ru.md.base_domain.user.model.RoleUser

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
	val schedule: String? = null,
	val birthDate: Long? = null,
	val jobDate: Long? = null,
	val roles: Set<RoleUser> = emptySet()
)
