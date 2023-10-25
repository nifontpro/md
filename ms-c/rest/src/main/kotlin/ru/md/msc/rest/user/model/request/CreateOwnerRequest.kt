package ru.md.msc.rest.user.model.request

import ru.md.base_domain.user.model.Gender

data class CreateOwnerRequest(
	val firstname: String = "",
	val lastname: String? = null,
	val patronymic: String? = null,
	val post: String? = null,
	val gender: Gender = Gender.UNDEF,
	val phone: String? = null,
	val address: String? = null,
	val description: String? = null,
)
