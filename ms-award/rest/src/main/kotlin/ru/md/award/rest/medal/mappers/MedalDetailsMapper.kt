package ru.md.award.rest.medal.mappers

import ru.md.award.domain.medal.model.MedalDetails
import ru.md.award.rest.medal.model.response.MedalDetailsResponse
import ru.md.base_domain.model.mappers.toEpochMilliUTC
import ru.md.base_rest.model.mapper.toBaseImageResponse

fun MedalDetails.toMedalDetailsResponse() = MedalDetailsResponse(
	medal = medal,
	description = description,
	createdAt = createdAt.toEpochMilliUTC(),
	images = images.map { it.toBaseImageResponse() }
)