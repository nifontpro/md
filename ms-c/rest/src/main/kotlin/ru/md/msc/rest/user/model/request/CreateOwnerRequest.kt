package ru.md.msc.rest.user.model.request

import com.fasterxml.jackson.annotation.JsonIgnore
import ru.md.msc.domain.user.model.Gender

data class CreateOwnerRequest(
	val firstname: String = "",
	val lastname: String? = null,
	val patronymic: String? = null,
	val post: String? = null,
	val gender: Gender = Gender.UNDEF,
	val phone: String? = null,
	val address: String? = null,
	val description: String? = null,

	@JsonIgnore
	val email: String = "",
	@JsonIgnore
	val emailVerified: Boolean = false
)
