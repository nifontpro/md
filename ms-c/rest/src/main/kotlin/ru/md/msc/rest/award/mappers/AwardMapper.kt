package ru.md.msc.rest.award.mappers

import ru.md.base_domain.model.converter.toEpochMilliUTC
import ru.md.msc.domain.award.model.Award
import ru.md.msc.rest.award.model.response.AwardResponse

fun Award.toAwardResponse() = AwardResponse(
	id = id,
	name = name,
	type = type,
	mainImg = mainImg,
	startDate = startDate.toEpochMilliUTC(),
	endDate = endDate.toEpochMilliUTC(),
	state = state,
	dept = dept,
	images = images,
	users = users
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