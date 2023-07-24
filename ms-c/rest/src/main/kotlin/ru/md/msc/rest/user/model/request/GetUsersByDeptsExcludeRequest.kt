package ru.md.msc.rest.user.model.request

import ru.md.base_rest.model.BaseRequest
import ru.md.msc.domain.award.model.ActionType

data class GetUsersByDeptsExcludeRequest(
	val authId: Long = 0,
	val deptId: Long = 0,

	// for exclude:
	val awardId: Long = 0,
	val actionType: ActionType? = null,

	val baseRequest: BaseRequest? = null
)