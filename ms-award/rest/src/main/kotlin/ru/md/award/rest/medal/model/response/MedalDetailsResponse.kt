package ru.md.award.rest.medal.model.response

import ru.md.award.domain.medal.model.Medal
import ru.md.base_rest.model.response.BaseImageResponse

data class MedalDetailsResponse(
	val medal: Medal = Medal(),
	val description: String? = null,
	val createdAt: Long = 0,
	val images: List<BaseImageResponse>
)
