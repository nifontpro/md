package ru.md.msc.rest.award.model.response

import ru.md.base_rest.model.response.BaseImageResponse

data class AwardDetailsResponse(
	val award: AwardResponse,
	val criteria: String? = null,
	val createdAt: Long? = null,
	val images: List<BaseImageResponse> = emptyList(),
)
