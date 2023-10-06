package ru.md.msc.rest.award.model.response

import ru.md.msc.domain.award.model.ActionType
import ru.md.msc.rest.dept.model.response.DeptResponse
import ru.md.msc.rest.user.model.response.UserResponse

data class ActivityResponse(
	val id: Long = 0,
	val date: Long? = null,
	val user: UserResponse? = null,
	val award: AwardResponse? = null,
	val actionType: ActionType = ActionType.UNDEF,
	val activ: Boolean = true,
	val dept: DeptResponse? = null,
	val authId: Long = 0,
)