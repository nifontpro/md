package ru.md.msc.rest.award.model.request

import ru.md.base_rest.model.request.BaseRequest
import ru.md.msc.domain.award.model.AwardState

data class GetAwardsBySubDeptsRequest(
	val authId: Long = 0,
	val state: AwardState? = null,
	val baseRequest: BaseRequest = BaseRequest()
)