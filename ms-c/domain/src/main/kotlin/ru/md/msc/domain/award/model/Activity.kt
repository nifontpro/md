package ru.md.msc.domain.award.model

import ru.md.base_domain.dept.model.Dept
import ru.md.base_domain.user.model.User
import java.time.LocalDateTime

data class Activity(
	val id: Long = 0,
	val date: LocalDateTime? = null,
	val user: User? = null,
	val award: Award? = null,
	val actionType: ActionType = ActionType.UNDEF,
	val activ: Boolean = true,
	val dept: Dept? = null,
	val authId: Long = 0,
)