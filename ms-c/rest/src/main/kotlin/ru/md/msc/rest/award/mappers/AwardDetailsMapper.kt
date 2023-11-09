package ru.md.msc.rest.award.mappers

import ru.md.base_domain.model.mappers.toEpochMilliUTC
import ru.md.base_rest.model.mapper.toBaseImageResponse
import ru.md.msc.domain.award.model.AwardDetails
import ru.md.msc.rest.award.model.response.AwardDetailsResponse

fun AwardDetails.toAwardDetailsResponse(): AwardDetailsResponse {
	val baseImages = images.map { it.toBaseImageResponse() }
	return AwardDetailsResponse(
		award = award.toAwardResponse(baseImages),
		criteria = criteria,
		createdAt = createdAt?.toEpochMilliUTC(),
		images = baseImages
	)
}