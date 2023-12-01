package ru.md.msc.domain.user.biz.workers.event

import ru.md.base_domain.biz.proc.ContextState
import ru.md.base_domain.constants.DB_BIRTHDAY_FIELD_NAME
import ru.md.base_domain.constants.DB_JOB_DATE_FIELD_NAME
import ru.md.cor.ICorChainDsl
import ru.md.cor.worker
import ru.md.msc.domain.event.model.UserEvent
import ru.md.msc.domain.user.biz.proc.UserContext
import ru.md.msc.domain.user.biz.proc.userEventError
import java.time.LocalDateTime

fun ICorChainDsl<UserContext>.addOrUpdateUserEvents(title: String) = worker {

	this.title = title
	on { state == ContextState.RUNNING }

	handle {
		addEvent(eventDate = birthDate, eventName = DB_BIRTHDAY_FIELD_NAME)
		addEvent(eventDate = jobDate, eventName = DB_JOB_DATE_FIELD_NAME)
	}
	except {
		log.error(it.message)
		userEventError()
	}
}

private fun UserContext.addEvent(eventDate: LocalDateTime?, eventName: String) {
	eventDate?.let {
		val userEvent = UserEvent(
			eventDate = it,
			eventName = eventName,
			userId = userId
		)
		eventService.addOrUpdateUserEvent(userEvent)
	}
}