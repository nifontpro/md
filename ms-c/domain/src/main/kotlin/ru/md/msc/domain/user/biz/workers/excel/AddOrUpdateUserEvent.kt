package ru.md.msc.domain.user.biz.workers.excel

import ru.md.base_domain.biz.helper.ContextError
import ru.md.base_domain.constants.DB_BIRTHDAY_FIELD_NAME
import ru.md.base_domain.constants.DB_JOB_DATE_FIELD_NAME
import ru.md.msc.domain.event.biz.proc.EventIOException
import ru.md.msc.domain.event.model.UserEvent
import ru.md.msc.domain.user.biz.proc.UserContext

internal fun UserContext.addOrUpdateUserEvent(
	userId: Long,
	birthDate: CellDate?,
	jobDate: CellDate?,
	userEvents: MutableList<UserEvent>,
	userErrors: MutableList<ContextError>,
): Boolean {
	var isUpdate = false

	birthDate?.let {
		if (it.success && it.date != null) {
			val userEvent = UserEvent(
				eventDate = it.date,
				eventName = DB_BIRTHDAY_FIELD_NAME,
				userId = userId
			)
			try {
				val event = eventService.addOrUpdateUserEvent(userEvent)
				userEvents.add(event)
				isUpdate = event.isUpdate
			} catch (e: Exception) {
				log.error(e.message)
				throw EventIOException()
			}
		} else {
			userErrors.add(parseDateError(field = it.field, date = it.text))
		}
	}

	jobDate?.let {
		if (it.success && it.date != null) {
			val userEvent = UserEvent(
				eventDate = it.date,
				eventName = DB_JOB_DATE_FIELD_NAME,
				userId = userId
			)
			try {
				val event = eventService.addOrUpdateUserEvent(userEvent)
				userEvents.add(event)
				isUpdate = event.isUpdate || isUpdate
			} catch (e: Exception) {
				log.error(e.message)
				throw EventIOException()
			}
		} else {
			userErrors.add(parseDateError(field = it.field, date = it.text))
		}
	}

	return isUpdate
}