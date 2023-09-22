package ru.md.msc.rest.user.model.response

data class UserDetailsDeptResponse(
	val userDetails: UserDetailsResponse,
	val deptId: Long,
)
