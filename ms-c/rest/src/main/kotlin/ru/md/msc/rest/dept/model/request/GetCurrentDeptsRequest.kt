package ru.md.msc.rest.dept.model.request

import ru.md.base_rest.model.BaseRequest

data class GetCurrentDeptsRequest(
	val authId: Long = 0,
	val deptId: Long = 0,
	val baseRequest: BaseRequest = BaseRequest()
)