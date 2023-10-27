package ru.md.msc.rest.award.mappers

import ru.md.base_domain.model.mappers.toEpochMilliUTC
import ru.md.msc.domain.award.model.Activity
import ru.md.msc.rest.award.model.response.ActivityResponse
import ru.md.msc.rest.dept.mappers.toDeptResponse
import ru.md.msc.rest.user.mappers.toUserResponse

fun Activity.toActivityResponse() = ActivityResponse(
	id = id,
	date = date?.toEpochMilliUTC(),
	user = user?.toUserResponse(),
	award = award?.toAwardResponse(),
	actionType = actionType,
	activ = activ,
	dept = dept?.toDeptResponse(),
	authId = authId
)