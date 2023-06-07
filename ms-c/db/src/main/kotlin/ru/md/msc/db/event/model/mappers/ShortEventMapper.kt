package ru.md.msc.db.event.model.mappers

import ru.md.msc.db.event.model.IShortEvent
import ru.md.msc.domain.event.model.ShortEvent

fun IShortEvent.toShortEvent() = ShortEvent(
	id = getId(),
	eventDate = getEventDate(),
	days = getDays(),
	eventName = getEventName(),
)