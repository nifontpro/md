package ru.md.msc.rest.award.mappers

import ru.md.base_domain.model.mappers.toEpochMilliUTC
import ru.md.msc.domain.award.model.Award
import ru.md.msc.rest.award.model.response.AwardResponse
import ru.md.msc.rest.dept.mappers.toDeptResponse
import ru.md.msc.rest.user.mappers.toUserResponse

fun Award.toAwardResponseWithUsers() = AwardResponse(
	id = id,
	name = name,
	description = description,
	type = type,
	mainImg = mainImg,
	normImg = normImg,
	startDate = startDate.toEpochMilliUTC(),
	endDate = endDate.toEpochMilliUTC(),
	score = score,
	state = state,
	dept = dept.toDeptResponse(),
	users = users.map { it.toUserResponse() }
)

fun Award.toAwardResponse() = AwardResponse(
	id = id,
	name = name,
	description = description,
	type = type,
	mainImg = mainImg,
	normImg = normImg,
	startDate = startDate.toEpochMilliUTC(),
	endDate = endDate.toEpochMilliUTC(),
	score = score,
	state = state,
	dept = dept.toDeptResponse(),
)