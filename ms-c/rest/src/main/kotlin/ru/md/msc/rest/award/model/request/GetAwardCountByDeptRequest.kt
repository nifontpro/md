package ru.md.msc.rest.award.model.request

data class GetAwardCountByDeptRequest(
	val authId: Long = 0,
	val deptId: Long = 0,
	val subdepts: Boolean = false
)