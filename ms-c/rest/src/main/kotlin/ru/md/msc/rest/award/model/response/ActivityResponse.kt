package ru.md.msc.rest.award.model.response

import ru.md.msc.domain.award.model.ActionType
import ru.md.msc.domain.user.model.User

data class ActivityResponse(
	val id: Long = 0,
	val date: Long? = null,
	val user: User? = null,
	val award: AwardResponse? = null,
	val actionType: ActionType = ActionType.UNDEF,
	val activ: Boolean = true,
	val deptId: Long = 0,
	val authId: Long = 0,
)