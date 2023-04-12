package ru.md.msc.rest.dept.model.request

data class CreateDeptRequest(
	val authId: Long = 0,
	val parentId: Long? = null,
	val name: String = "",
	val classname: String? = null,
	val address: String? = null,
	val email: String? = null,
	val phone: String? = null,
	val description: String? = null,
)