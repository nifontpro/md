package ru.md.msc.domain.event.biz.validate

import ru.md.base_domain.biz.helper.errorValidation
import ru.md.base_domain.biz.helper.fail
import ru.md.cor.ICorChainDsl
import ru.md.cor.worker
import ru.md.msc.domain.event.biz.proc.EventsContext

fun ICorChainDsl<EventsContext>.validateEventId(title: String) = worker {
	this.title = title
	on { eventId < 1 }
	handle {
		fail(
			errorValidation(
				field = "eventId",
				violationCode = "not valid",
				description = "Неверный eventId"
			)
		)
	}
}
