package ru.md.msc.rest.award.mappers

import ru.md.msc.domain.award.model.AwardDetails
import ru.md.msc.domain.base.model.converter.toEpochMilliUTC
import ru.md.msc.rest.award.model.response.AwardDetailsResponse

fun AwardDetails.toAwardDetailsResponse() = AwardDetailsResponse(
	award = award.toAwardResponse(),
	description = description,
	criteria = criteria,
	createdAt = createdAt?.toEpochMilliUTC()
)