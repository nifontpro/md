package ru.md.msc.domain.message.biz.proc

import ru.md.msc.domain.base.biz.BaseClientContext
import ru.md.base_domain.biz.helper.errorDb
import ru.md.base_domain.biz.helper.fail

fun BaseClientContext.messageNotFoundError() {
	fail(
		errorDb(
			repository = "message",
			violationCode = "not found",
			description = "Сообщение не найдено"
		)
	)
}

fun BaseClientContext.getMessageError() {
	fail(
		errorDb(
			repository = "message",
			violationCode = "get error",
			description = "Ошибка получения сообщения"
		)
	)
}

fun BaseClientContext.updateMessageError() {
	fail(
		errorDb(
			repository = "message",
			violationCode = "modify",
			description = "Ошибка изменения сообщения"
		)
	)
}

class MessageNotFoundException(message: String = "") : RuntimeException(message)