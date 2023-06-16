package ru.md.msc.rest.dept.model.request

data class UpdateDeptRequest(
	val authId: Long = 0,
	val deptId: Long = 0,
	val name: String = "",
	val classname: String? = null,
	val topLevel: Boolean = false,
	val address: String? = null,
	val email: String? = null,
	val phone: String? = null,
	val description: String? = null,
)