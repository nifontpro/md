package ru.md.msc.db.event.model.mappers

import ru.md.msc.db.event.model.DeptEventEntity
import ru.md.msc.domain.event.model.BaseEvent

fun DeptEventEntity.toBaseEvent() = BaseEvent(
	id = id ?: 0,
	eventDate = eventDate,
	eventName = eventName,
	entityName = "Dept with id: $deptId",
	deptId = deptId,
)