package ru.md.msc.rest.award.mappers

import ru.md.base_domain.model.converter.toEpochMilliUTC
import ru.md.msc.domain.award.model.Activity
import ru.md.msc.rest.award.model.response.ActivityResponse

fun Activity.toActivityResponse() = ActivityResponse(
	id = id,
	date = date?.toEpochMilliUTC(),
	user = user,
	award = award?.toAwardResponse(),
	actionType = actionType,
	activ = activ,
	deptId = deptId,
	authId = authId
)