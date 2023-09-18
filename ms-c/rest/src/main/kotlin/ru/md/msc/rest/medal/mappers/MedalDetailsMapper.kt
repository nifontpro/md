package ru.md.msc.rest.medal.mappers

import ru.md.base_domain.model.converter.toEpochMilliUTC
import ru.md.msc.domain.medal.model.MedalDetails
import ru.md.msc.rest.medal.model.response.MedalDetailsResponse

fun MedalDetails.toMedalDetailsResponse() = MedalDetailsResponse(
	medal = medal,
	description = description,
	createdAt = createdAt.toEpochMilliUTC()
)