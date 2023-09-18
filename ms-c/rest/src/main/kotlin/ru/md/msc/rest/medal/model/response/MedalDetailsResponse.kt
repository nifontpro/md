package ru.md.msc.rest.medal.model.response

import ru.md.base_rest.model.response.BaseImageResponse
import ru.md.msc.domain.medal.model.Medal

data class MedalDetailsResponse(
	val medal: Medal = Medal(),
	val description: String? = null,
	val createdAt: Long = 0,
	val images: List<BaseImageResponse>
)
