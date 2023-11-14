package ru.md.msc.rest.dept.model.request

data class GetDeptSettingsRequest(
	val authId: Long = 0,
	val deptId: Long = 0,
)