package ru.md.base_domain.user.model

import ru.md.base_domain.dept.model.Dept
import java.util.Collections.emptySet

data class User(
	val id: Long = 0,
	val dept: Dept? = null,
	val firstname: String = "",
	val patronymic: String? = null,
	val lastname: String? = null,
	val authEmail: String? = null,
	val gender: Gender = Gender.UNDEF,
	val post: String? = null,
	val roles: Set<RoleUser> = emptySet(),
	val mainImg: String? = null,
	val normImg: String? = null,
	val archive: Boolean = false,
	val awardCount: Long = 0,
	val scores: Long = 0,
)

fun getFirstAndLastName(user: User) = "${user.firstname} ${user.lastname ?: ""}"
fun getFullName(user: User) = "${user.firstname} ${user.patronymic ?: ""} ${user.lastname ?: ""}"
fun getFIO(user: User) = "${user.lastname ?: "-"} ${user.firstname.first()}. ${user.patronymic?.first() ?: ""}"