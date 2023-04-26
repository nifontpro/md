package ru.md.msc.rest.dept.model.request

data class DeleteDeptImageRequest(
	val authId: Long = 0,
	val deptId: Long = 0,
	val imageId: Long = 0,
)