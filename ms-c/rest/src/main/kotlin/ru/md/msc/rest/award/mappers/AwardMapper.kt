package ru.md.msc.rest.award.mappers

import ru.md.msc.domain.award.model.Award
import ru.md.msc.domain.base.model.converter.toEpochMilliUTC
import ru.md.msc.rest.award.model.response.AwardResponse

fun Award.toAwardResponse() = AwardResponse(
	id = id,
	name = name,
	type = type,
	startDate = startDate.toEpochMilliUTC(),
	endDate = endDate.toEpochMilliUTC(),
	dept = dept,
	images = images
)