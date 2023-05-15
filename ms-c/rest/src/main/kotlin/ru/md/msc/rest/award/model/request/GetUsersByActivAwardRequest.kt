package ru.md.msc.rest.award.model.request

import ru.md.base_rest.model.BaseRequest

data class GetUsersByActivAwardRequest(
	val authId: Long = 0,
	val awardId: Long = 0,
	val baseRequest: BaseRequest = BaseRequest()
)