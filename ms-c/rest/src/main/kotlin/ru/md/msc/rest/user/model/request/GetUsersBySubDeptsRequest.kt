package ru.md.msc.rest.user.model.request

import ru.md.msc.rest.base.BaseRequest

data class GetUsersBySubDeptsRequest(
	val authId: Long = 0,
	val deptId: Long = 0,
	val baseRequest: BaseRequest
)