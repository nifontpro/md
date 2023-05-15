package ru.md.msc.domain.user.biz.proc

import ru.md.msc.domain.base.biz.BaseClientContext
import ru.md.base_domain.biz.helper.errorDb
import ru.md.base_domain.biz.helper.fail

fun BaseClientContext.userNotFoundError() {
	fail(
		errorDb(
			repository = "user",
			violationCode = "not found",
			description = "Сотрудник не найден"
		)
	)
}

fun BaseClientContext.getUserError() {
	fail(
		errorDb(
			repository = "user",
			violationCode = "get error",
			description = "Ошибка получения сотрудника"
		)
	)
}

class UserNotFoundException(message: String = "") : RuntimeException(message)