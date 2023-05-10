package ru.md.msc.rest.award.model.request

import ru.md.msc.rest.base.BaseRequest

data class GetAwardsBySubDeptsRequest(
	val authId: Long = 0,
	val baseRequest: BaseRequest = BaseRequest()
)