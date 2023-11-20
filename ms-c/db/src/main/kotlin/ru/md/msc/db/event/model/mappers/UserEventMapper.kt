package ru.md.msc.db.event.model.mappers

import ru.md.msc.db.event.model.UserEventEntity
import ru.md.msc.domain.event.model.BaseEvent
import ru.md.msc.domain.event.model.UserEvent

fun UserEventEntity.toBaseEvent() = BaseEvent(
	id = id ?: 0,
	eventDate = eventDate,
	eventName = eventName,
	entityName = "User with id: $userId",
	userId = userId,
)

fun UserEventEntity.toUserEvent(isUpdate: Boolean) = UserEvent(
	id = id ?: 0,
	userId = userId,
	eventDate = eventDate,
	eventName = eventName,
	isUpdate = isUpdate
)