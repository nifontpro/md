package ru.md.msc.rest.award.mappers

import ru.md.base_domain.model.converter.toEpochMilliUTC
import ru.md.msc.domain.award.model.Activity
import ru.md.msc.rest.award.model.response.ActivityResponse
import ru.md.msc.rest.dept.mappers.toDeptResponse
import ru.md.msc.rest.user.mappers.toUserResponseSimple

fun Activity.toActivityResponse() = ActivityResponse(
	id = id,
	date = date?.toEpochMilliUTC(),
	user = user?.toUserResponseSimple(),
	award = award?.toAwardResponseSimple(),
	actionType = actionType,
	activ = activ,
	dept = dept?.toDeptResponse(),
	authId = authId
)