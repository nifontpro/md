package ru.md.msc.rest.award.model.response

import ru.md.msc.domain.award.model.ActionType
import ru.md.msc.domain.dept.model.Dept
import ru.md.msc.domain.user.model.User

data class ActivityResponse(
	val id: Long = 0,
	val date: Long? = null,
	val user: User? = null,
	val award: AwardResponse? = null,
	val actionType: ActionType = ActionType.UNDEF,
	val activ: Boolean = true,
	val dept: Dept? = null,
	val authId: Long = 0,
)