package ru.md.msc.db.event.model.mappers

import ru.md.msc.db.event.model.IBaseEvent
import ru.md.msc.domain.event.model.BaseEvent

fun IBaseEvent.toBaseEvent() = BaseEvent(
	id = getId(),
	eventDate = getEventDate(),
	days = getDays(),
	eventName = getEventName(),
	entityName = getEntityName(),
	imageUrl = getImageUrl(),
	userId = getUserId(),
	deptId = getDeptId(),
	deptName = getDeptName(),
	deptClassname = getDeptClassname()
)