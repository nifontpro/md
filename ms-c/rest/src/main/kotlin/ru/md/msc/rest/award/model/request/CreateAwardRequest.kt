package ru.md.msc.rest.award.model.request

import ru.md.msc.domain.award.model.AwardType

data class CreateAwardRequest(
	val authId: Long = 0,
	val deptId: Long = 0,

	val name: String = "",
	val type: AwardType = AwardType.UNDEF,
	val startDate: Long = 0,
	val endDate: Long = 0,

	val description: String? = null,
	val criteria: String? = null
)