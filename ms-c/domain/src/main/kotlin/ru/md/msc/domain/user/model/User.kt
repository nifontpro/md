package ru.md.msc.domain.user.model

import ru.md.msc.domain.dept.model.Dept
import java.util.Collections.emptySet

data class User(
	val id: Long = 0,
	val dept: Dept? = null,
	val firstname: String = "",
	val patronymic: String? = null,
	val lastname: String? = null,
	val authEmail: String = "",
	val gender: Gender = Gender.UNDEF,
	val post: String? = null,
	val roles: Set<RoleUser> = emptySet(),
)