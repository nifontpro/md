package ru.md.msc.rest.dept.model.request

data class GetDeptByIdRequest (
	val authId: Long = 0,
	val deptId: Long = 0
)