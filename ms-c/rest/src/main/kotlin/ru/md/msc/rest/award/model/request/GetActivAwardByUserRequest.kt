package ru.md.msc.rest.award.model.request

import ru.md.base_rest.model.BaseRequest

data class GetActivAwardByUserRequest(
	val authId: Long = 0,
	val userId: Long = 0,
	val baseRequest: BaseRequest = BaseRequest()
)