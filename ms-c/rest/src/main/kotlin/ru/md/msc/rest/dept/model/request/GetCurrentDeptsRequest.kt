package ru.md.msc.rest.dept.model.request

import ru.md.base_rest.model.request.BaseRequest

data class GetCurrentDeptsRequest(
	val authId: Long = 0,
	val parentId: Long = 0,
	val baseRequest: BaseRequest = BaseRequest()
)