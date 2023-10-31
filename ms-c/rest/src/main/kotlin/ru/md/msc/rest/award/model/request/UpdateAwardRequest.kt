package ru.md.msc.rest.award.model.request

import ru.md.msc.domain.award.model.AwardType

data class UpdateAwardRequest(
	val authId: Long = 0,
	val awardId: Long = 0,

	val name: String = "",
	val type: AwardType = AwardType.UNDEF,
	val startDate: Long = 0,
	val endDate: Long = 0,
	val score: Int = 1,

	val description: String? = null,
	val criteria: String? = null
)