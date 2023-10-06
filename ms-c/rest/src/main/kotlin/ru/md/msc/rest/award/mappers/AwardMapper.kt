package ru.md.msc.rest.award.mappers

import ru.md.base_domain.model.converter.toEpochMilliUTC
import ru.md.base_rest.model.mapper.toBaseImageResponse
import ru.md.msc.domain.award.model.Award
import ru.md.msc.rest.award.model.response.AwardResponse
import ru.md.msc.rest.dept.mappers.toDeptResponse
import ru.md.msc.rest.user.mappers.toUserResponse
import ru.md.msc.rest.user.mappers.toUserResponseSimple

fun Award.toAwardResponse() = AwardResponse(
	id = id,
	name = name,
	type = type,
	mainImg = mainImg,
	startDate = startDate.toEpochMilliUTC(),
	endDate = endDate.toEpochMilliUTC(),
	score = score,
	state = state,
	dept = dept.toDeptResponse(),
	images = images.map { it.toBaseImageResponse() },
	users = users.map { it.toUserResponse() }
)

fun Award.toAwardResponseSimple() = AwardResponse(
	id = id,
	name = name,
	type = type,
	mainImg = mainImg,
	startDate = startDate.toEpochMilliUTC(),
	endDate = endDate.toEpochMilliUTC(),
	score = score,
	state = state,
	dept = dept.toDeptResponse(),
	images = images.map { it.toBaseImageResponse() },
	users = users.map { it.toUserResponseSimple() }
)



//fun Award.getState(): AwardState {
//	val now = LocalDateTime.now()
//	return when {
//		endDate < startDate -> AwardState.ERROR
//		now >= startDate && now <= endDate -> AwardState.NOMINEE
//		now < startDate -> AwardState.FUTURE
//		else -> AwardState.FINISH
//	}
//}