package ru.md.msc.rest.event.mappers

import ru.md.base_domain.model.converter.toEpochMilliUTC
import ru.md.msc.domain.event.model.BaseEvent
import ru.md.msc.rest.event.model.response.BaseEventResponse

fun BaseEvent.toBaseEventResponse() = BaseEventResponse(
	id = id,
	eventDate = eventDate.toEpochMilliUTC(),
	days =days,
	eventName = eventName,
	entityName = entityName,
	imageUrl = imageUrl,
	userId = userId,
	deptId = deptId,
	deptName = deptName
)