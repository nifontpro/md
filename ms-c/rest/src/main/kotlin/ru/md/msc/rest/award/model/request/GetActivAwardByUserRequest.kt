package ru.md.msc.rest.award.model.request

import ru.md.base_rest.model.BaseRequest
import ru.md.msc.domain.award.model.AwardState

data class GetActivAwardByUserRequest(
	val authId: Long = 0,
	val userId: Long = 0,
	val awardState: AwardState? = null,
	val baseRequest: BaseRequest = BaseRequest()
)