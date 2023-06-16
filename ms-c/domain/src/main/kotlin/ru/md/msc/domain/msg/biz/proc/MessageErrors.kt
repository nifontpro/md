package ru.md.msc.domain.msg.biz.proc

import ru.md.msc.domain.base.biz.BaseClientContext
import ru.md.base_domain.biz.helper.errorDb
import ru.md.base_domain.biz.helper.fail

fun MessageContext.messageNotFoundError() {
	fail(
		errorDb(
			repository = "message",
			violationCode = "not found",
			description = "Сообщение не найдено"
		)
	)
}

fun MessageContext.getMessageError() {
	fail(
		errorDb(
			repository = "message",
			violationCode = "get error",
			description = "Ошибка получения сообщения"
		)
	)
}

fun MessageContext.updateMessageError() {
	fail(
		errorDb(
			repository = "message",
			violationCode = "modify",
			description = "Ошибка изменения сообщения"
		)
	)
}

fun BaseClientContext.sendMessageError() {
	fail(
		errorDb(
			repository = "message",
			violationCode = "send",
			description = "Ошибка отправки сообщения сотруднику"
		)
	)
}

class MessageNotFoundException(message: String = "") : RuntimeException(message)