package ru.md.msc.domain.event.biz.workers

import ru.md.base_domain.biz.proc.ContextState
import ru.md.cor.ICorChainDsl
import ru.md.cor.worker
import ru.md.msc.domain.event.biz.proc.EventsContext
import ru.md.msc.domain.event.biz.proc.deleteEventError

fun ICorChainDsl<EventsContext>.deleteUserEvent(title: String) = worker {

	this.title = title
	on { state == ContextState.RUNNING }

	handle {
		eventService.deleteUserEventById(eventId = eventId)
	}

	except {
		log.error(it.message)
		deleteEventError()
	}
}