package ru.md.msc.rest.award.model.request

import ru.md.base_rest.model.BaseRequest
import ru.md.msc.domain.award.model.ActionType

data class GetUsersByActivAwardRequest(
	val authId: Long = 0,
	val awardId: Long = 0,
	val actionType: ActionType = ActionType.UNDEF,
	val baseRequest: BaseRequest = BaseRequest()
)