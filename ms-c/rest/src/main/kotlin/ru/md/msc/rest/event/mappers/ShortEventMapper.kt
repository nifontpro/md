package ru.md.msc.rest.event.mappers

import ru.md.base_domain.model.mappers.toEpochMilliUTC
import ru.md.msc.domain.event.model.ShortEvent
import ru.md.msc.rest.event.model.response.ShortEventResponse

fun ShortEvent.toShortEventResponse() = ShortEventResponse(
	id = id,
	eventDate = eventDate.toEpochMilliUTC(),
	days = days,
	eventName = eventName,
)