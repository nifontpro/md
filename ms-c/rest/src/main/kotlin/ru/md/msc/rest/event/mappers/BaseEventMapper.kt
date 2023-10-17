package ru.md.msc.rest.event.mappers

import ru.md.base_domain.model.converter.toEpochMilliUTC
import ru.md.msc.domain.event.model.BaseEvent
import ru.md.msc.rest.event.model.response.BaseEventResponse
import ru.md.msc.rest.event.model.response.EventType

fun BaseEvent.toBaseEventResponse() = BaseEventResponse(
	id = id,
	eventDate = eventDate.toEpochMilliUTC(),
	days = days,
	eventName = eventName,
	entityName = entityName,
	imageUrl = imageUrl,
	userId = userId,
	deptId = deptId,
	deptName = deptName,
	eventType = if (userId == 0L) {
		when (deptLevel) {
			0 -> EventType.ROOT
			1 -> EventType.OWNER
			2 -> EventType.COMPANY
			in 3..Int.MAX_VALUE -> EventType.DEPT
			else -> EventType.ERROR
		}
	} else {
		EventType.USER
	},
	deptLevel = deptLevel
)