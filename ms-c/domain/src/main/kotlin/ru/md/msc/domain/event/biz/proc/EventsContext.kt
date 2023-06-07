package ru.md.msc.domain.event.biz.proc

import ru.md.base_domain.biz.proc.IBaseCommand
import ru.md.msc.domain.base.biz.BaseClientContext
import ru.md.msc.domain.event.model.BaseEvent
import ru.md.msc.domain.event.model.ShortEvent
import ru.md.msc.domain.event.service.EventService

class EventsContext : BaseClientContext() {
	var baseEvent: BaseEvent = BaseEvent()
	var events: List<BaseEvent> = emptyList()
	var shortEvents: List<ShortEvent> = emptyList()

	lateinit var eventService: EventService
}

enum class EventCommand : IBaseCommand {
	ADD_USER_EVENT,
	ADD_DEPT_EVENT,
	GET_ALL_EVENTS,
	GET_USER_EVENTS,
	GET_DEPT_EVENTS,
}
