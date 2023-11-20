package ru.md.msc.domain.event.biz.proc

import ru.md.base_domain.biz.helper.errorDb
import ru.md.base_domain.biz.helper.fail

fun EventsContext.eventNotFoundError() {
	fail(
		errorDb(
			repository = "event",
			violationCode = "not found",
			description = "Событие не найдено"
		)
	)
}

fun EventsContext.getEventsError() {
	fail(
		errorDb(
			repository = "event",
			violationCode = "get error",
			description = "Ошибка получения событий"
		)
	)
}

fun EventsContext.deleteEventError() {
	fail(
		errorDb(
			repository = "event",
			violationCode = "delete",
			description = "Ошибка удаления события"
		)
	)
}

class EventNotFoundException(message: String = "") : RuntimeException(message)
class EventIOException(message: String = "") : RuntimeException(message)
