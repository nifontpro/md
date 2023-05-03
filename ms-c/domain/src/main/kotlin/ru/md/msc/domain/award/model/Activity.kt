package ru.md.msc.domain.award.model

import ru.md.msc.domain.user.model.User
import java.time.LocalDateTime

data class Activity(
	val id: Long = 0,
	val date: LocalDateTime = LocalDateTime.now(),
	val user: User = User(),
	val award: Award = Award(),
	val actionType: ActionType = ActionType.UNDEF,
	val activ: Boolean = true,
	val deptId: Long = 0,
	val authId: Long = 0,
)