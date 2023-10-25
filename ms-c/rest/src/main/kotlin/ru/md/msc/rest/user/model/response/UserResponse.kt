package ru.md.msc.rest.user.model.response

import ru.md.base_rest.model.response.BaseImageResponse
import ru.md.base_domain.dept.model.Dept
import ru.md.base_domain.user.model.Gender
import ru.md.base_domain.user.model.RoleUser

data class UserResponse(
	val id: Long = 0,
	val dept: Dept? = null,
	val firstname: String = "",
	val patronymic: String? = null,
	val lastname: String? = null,
	val authEmail: String? = null,
	val gender: Gender = Gender.UNDEF,
	val post: String? = null,
	val awardCount: Long = 0,
	val scores: Long = 0,
	val mainImg: String? = null,
	val roles: Set<RoleUser> = emptySet(),
	val images: List<BaseImageResponse> = emptyList(),
	val archive: Boolean = false
)
