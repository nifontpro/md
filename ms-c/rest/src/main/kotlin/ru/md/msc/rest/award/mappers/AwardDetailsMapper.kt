package ru.md.msc.rest.award.mappers

import ru.md.base_domain.model.mappers.toEpochMilliUTC
import ru.md.msc.domain.award.model.AwardDetails
import ru.md.msc.rest.award.model.response.AwardDetailsResponse

fun AwardDetails.toAwardDetailsResponse() = AwardDetailsResponse(
	award = award.toAwardResponseWithUsers(),
	description = description,
	criteria = criteria,
	createdAt = createdAt?.toEpochMilliUTC()
)