package ru.md.msc.rest.event.model.request

data class GetDeptEventsRequest(
	val authId: Long = 0,
	val deptId: Long = 0,
)