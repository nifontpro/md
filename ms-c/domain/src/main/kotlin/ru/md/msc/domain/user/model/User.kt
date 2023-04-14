package ru.md.msc.domain.user.model

import ru.md.msc.domain.dept.model.Dept
import java.util.Collections.emptySet

data class User(
	val id: Long? = null,
	val dept: Dept? = null,
	val firstname: String = "",
	val patronymic: String? = null,
	val lastname: String? = null,
	val authEmail: String = "",
	val gender: Gender? = null,
	val post: String? = null,
	val roles: Set<RoleUser> = emptySet(),
)