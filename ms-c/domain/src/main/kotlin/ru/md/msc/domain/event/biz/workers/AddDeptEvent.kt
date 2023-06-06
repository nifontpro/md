package ru.md.msc.domain.event.biz.workers

import ru.md.base_domain.biz.helper.errorDb
import ru.md.base_domain.biz.helper.fail
import ru.md.base_domain.biz.proc.ContextState
import ru.md.cor.ICorChainDsl
import ru.md.cor.worker
import ru.md.msc.domain.event.biz.proc.EventsContext

fun ICorChainDsl<EventsContext>.addDeptEvent(title: String) = worker {

	this.title = title
	on { state == ContextState.RUNNING }

	handle {
		baseEvent = eventService.addDeptEvent(deptId = deptId, baseEvent = baseEvent)
	}

	except {
		log.error(it.message)
		fail(
			errorDb(
				repository = "event",
				violationCode = "add dept event",
				description = "Ошибка добавления события отдела"
			)
		)
	}
}