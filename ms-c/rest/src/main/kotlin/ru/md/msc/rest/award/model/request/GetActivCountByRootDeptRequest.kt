package ru.md.msc.rest.award.model.request

import ru.md.base_rest.model.request.BaseRequest

data class GetActivCountByRootDeptRequest(
	val authId: Long = 0,
	val baseRequest: BaseRequest = BaseRequest()
)