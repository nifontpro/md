package ru.md.msc.domain.event.biz.workers

import ru.md.base_domain.biz.helper.errorDb
import ru.md.base_domain.biz.helper.fail
import ru.md.base_domain.biz.helper.pageFun
import ru.md.base_domain.biz.proc.ContextState
import ru.md.cor.ICorChainDsl
import ru.md.cor.worker
import ru.md.msc.domain.event.biz.proc.EventsContext

fun ICorChainDsl<EventsContext>.getEvents(title: String) = worker {

	this.title = title
	on { state == ContextState.RUNNING }

	handle {
		events = pageFun { eventService.getEvents(deptId = deptId, baseQuery = baseQuery) }
		println(events)
	}

	except {
		log.error(it.message)
		fail(
			errorDb(
				repository = "event",
				violationCode = "get error",
				description = "Ошибка получения событий"
			)
		)
	}
}