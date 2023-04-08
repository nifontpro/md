package ru.md.msc.domain.user.model

import java.util.Collections.emptySet

data class User(
	val id: Long? = null,
	val email: String? = null,
	val firstname: String = "",
	val patronymic: String? = null,
	val lastname: String? = null,
	val gender: Gender? = null,
	val post: String? = null,
	val roles: Set<RoleEnum> = emptySet(),
)