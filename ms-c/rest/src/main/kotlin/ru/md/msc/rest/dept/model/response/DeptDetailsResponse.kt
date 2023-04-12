package ru.md.msc.rest.dept.model.response

import ru.md.msc.domain.dept.model.Dept

data class DeptDetailsResponse(
	val dept: Dept? = null,
	val address: String? = null,
	val email: String? = null,
	val phone: String? = null,
	val description: String? = null,
	val createdAt: Long? = null,
)
