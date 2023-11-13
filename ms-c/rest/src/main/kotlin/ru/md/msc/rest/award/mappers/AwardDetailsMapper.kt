package ru.md.msc.rest.award.mappers

import ru.md.base_domain.model.mappers.toEpochMilliUTC
import ru.md.base_rest.model.mapper.toBaseImageResponse
import ru.md.msc.domain.award.model.AwardDetails
import ru.md.msc.rest.award.model.response.AwardDetailsResponse

fun AwardDetails.toAwardDetailsResponse(): AwardDetailsResponse {
	return AwardDetailsResponse(
		award = award.toAwardResponse(),
		criteria = criteria,
		createdAt = createdAt?.toEpochMilliUTC(),
		images = images.map { it.toBaseImageResponse() }
	)
}