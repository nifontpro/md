package ru.md.msc.rest.event.model.request

import ru.md.base_rest.model.BaseRequest

data class GetAllEventsRequest(
	val authId: Long = 0,
	val deptId: Long = 0,
	val baseRequest: BaseRequest = BaseRequest()
)