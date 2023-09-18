package ru.md.msc.rest.award.model.request

import ru.md.base_rest.model.request.BaseRequest
import ru.md.msc.domain.award.model.AwardType

data class GetActivAwardByUserRequest(
	val authId: Long = 0,
	val userId: Long = 0,
	val awardType: AwardType? = null,
	val baseRequest: BaseRequest = BaseRequest()
)