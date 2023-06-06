package ru.md.msc.rest.event.mappers

import ru.md.base_domain.model.converter.toLocalDateTimeUTC
import ru.md.base_rest.model.mapper.toBaseQuery
import ru.md.msc.domain.event.biz.proc.EventCommand
import ru.md.msc.domain.event.biz.proc.EventsContext
import ru.md.msc.domain.event.model.BaseEvent
import ru.md.msc.rest.event.model.request.AddDeptEventRequest
import ru.md.msc.rest.event.model.request.AddUserEventRequest
import ru.md.msc.rest.event.model.request.GetAllEventsRequest

fun EventsContext.fromTransport(request: AddUserEventRequest) {
	command = EventCommand.ADD_USER_EVENT
	authId = request.authId
	userId = request.userId
	baseEvent = BaseEvent(
		eventDate = request.eventDate.toLocalDateTimeUTC(),
		eventName = request.eventName
	)
}

fun EventsContext.fromTransport(request: AddDeptEventRequest) {
	command = EventCommand.ADD_DEPT_EVENT
	authId = request.authId
	deptId = request.deptId
	baseEvent = BaseEvent(
		eventDate = request.eventDate.toLocalDateTimeUTC(),
		eventName = request.eventName
	)
}

fun EventsContext.fromTransport(request: GetAllEventsRequest) {
	command = EventCommand.GET_EVENTS
	authId = request.authId
	deptId = request.deptId
	baseQuery = request.baseRequest.toBaseQuery()
}