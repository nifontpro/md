package ru.md.msc.domain.user.biz.workers.event

import ru.md.base_domain.biz.proc.ContextState
import ru.md.base_domain.constants.DB_BIRTHDAY_FIELD_NAME
import ru.md.base_domain.constants.DB_JOB_DATE_FIELD_NAME
import ru.md.cor.ICorChainDsl
import ru.md.cor.worker
import ru.md.msc.domain.user.biz.proc.UserContext
import ru.md.msc.domain.user.biz.proc.userEventError

fun ICorChainDsl<UserContext>.addBaseEventToUserDetails(title: String) = worker {

	this.title = title
	on { state == ContextState.RUNNING }

	handle {
		val events = eventService.getEventsByUser(userId)
		val birthDate = events.find { it.eventName == DB_BIRTHDAY_FIELD_NAME }?.eventDate
		val jobDate = events.find { it.eventName == DB_JOB_DATE_FIELD_NAME }?.eventDate
		userDetails = userDetails.copy(
			birthDate = birthDate,
			jobDate = jobDate
		)
	}

	except {
		log.error(it.message)
		userEventError()
	}

}
