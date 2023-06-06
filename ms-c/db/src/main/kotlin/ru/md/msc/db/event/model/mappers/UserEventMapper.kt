package ru.md.msc.db.event.model.mappers

import ru.md.msc.db.event.model.UserEventEntity
import ru.md.msc.domain.event.model.BaseEvent

fun UserEventEntity.toBaseEvent() = BaseEvent(
	id = id ?: 0,
	eventDate = eventDate,
	eventName = eventName,
	entityName = "User with id: $userId",
	userId = userId,
)