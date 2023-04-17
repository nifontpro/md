package ru.md.msc.rest.user.model.request

data class GetUsersByDeptRequest(
	val authId: Long = 0,
	val deptId: Long = 0
)