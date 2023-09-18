package ru.md.msc.rest.dept.model.request

import ru.md.base_rest.model.request.BaseRequest

data class GetAuthSubtreeDeptsRequest(
	val authId: Long = 0,
	val baseRequest: BaseRequest = BaseRequest()
)