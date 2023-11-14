package ru.md.msc.rest.dept.model.request

data class SaveDeptSettingsRequest(
	val authId: Long = 0,
	val deptId: Long = 0,
	val payName: String = "",
)