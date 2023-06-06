package ru.md.msc.rest.event.model.request

data class AddDeptEventRequest(
	val authId: Long = 0,
	val deptId: Long = 0,
	val eventDate: Long = 0,
	val eventName: String = "",
)