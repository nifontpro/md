package ru.md.msc.rest.event.mappers

import ru.md.base_domain.model.converter.toLocalDateTimeUTC
import ru.md.msc.domain.event.biz.proc.EventCommand
import ru.md.msc.domain.event.biz.proc.EventsContext
import ru.md.msc.domain.event.model.BaseEvent
import ru.md.msc.rest.event.model.request.AddUserEventRequest

fun EventsContext.fromTransport(request: AddUserEventRequest) {
	command = EventCommand.ADD_USER_EVENT
	authId = request.authId
	userId = request.userId
	baseEvent = BaseEvent(
		eventDate = request.eventDate.toLocalDateTimeUTC(),
		eventName = request.eventName
	)
}