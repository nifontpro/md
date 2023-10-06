package ru.md.msc.rest.dept.model.response

data class DeptDetailsResponse(
	val dept: DeptResponse? = null,
	val address: String? = null,
	val email: String? = null,
	val phone: String? = null,
	val description: String? = null,
	val createdAt: Long? = null,
)
