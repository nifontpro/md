package ru.md.msc.rest.award.model.request

import ru.md.msc.domain.award.model.ActionType

data class AwardUserRequest(
	val authId: Long = 0,
	val awardId: Long = 0,
	val userId: Long = 0,
	val actionType: ActionType
)