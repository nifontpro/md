package ru.md.msc.rest.award.model.response

data class AwardDetailsResponse(
	val award: AwardResponse,
	val description: String? = null,
	val criteria: String? = null,
	val createdAt: Long? = null,
)
