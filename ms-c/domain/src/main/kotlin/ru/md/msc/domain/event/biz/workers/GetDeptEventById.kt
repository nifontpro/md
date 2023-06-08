package ru.md.msc.domain.event.biz.workers

import ru.md.base_domain.biz.proc.ContextState
import ru.md.cor.ICorChainDsl
import ru.md.cor.worker
import ru.md.msc.domain.event.biz.proc.EventNotFoundException
import ru.md.msc.domain.event.biz.proc.EventsContext
import ru.md.msc.domain.event.biz.proc.eventNotFoundError
import ru.md.msc.domain.event.biz.proc.getEventsError

fun ICorChainDsl<EventsContext>.getDeptEventById(title: String) = worker {

	this.title = title
	on { state == ContextState.RUNNING }

	handle {
		baseEvent = eventService.getDeptEventById(eventId = eventId)
	}

	except {
		log.error(it.message)
		when (it) {
			is EventNotFoundException -> eventNotFoundError()
			else -> getEventsError()
		}
	}
}