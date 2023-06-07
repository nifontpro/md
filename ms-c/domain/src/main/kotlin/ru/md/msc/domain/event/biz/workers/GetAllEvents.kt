package ru.md.msc.domain.event.biz.workers

import ru.md.base_domain.biz.helper.pageFun
import ru.md.base_domain.biz.proc.ContextState
import ru.md.cor.ICorChainDsl
import ru.md.cor.worker
import ru.md.msc.domain.event.biz.proc.EventsContext
import ru.md.msc.domain.event.biz.proc.getEventsError

fun ICorChainDsl<EventsContext>.getAllEvents(title: String) = worker {

	this.title = title
	on { state == ContextState.RUNNING }

	handle {
		events = pageFun { eventService.getEvents(deptId = deptId, baseQuery = baseQuery) }
	}

	except {
		log.error(it.message)
		getEventsError()
	}
}