package ru.md.msc.domain.user.biz.proc

import ru.md.msc.domain.base.biz.BaseContext
import ru.md.msc.domain.base.helper.errorDb
import ru.md.msc.domain.base.helper.fail

fun UserContext.userNotFoundError() {
	fail(
		errorDb(
			repository = "user",
			violationCode = "not found",
			description = "Сотрудник не найден"
		)
	)
}

fun BaseContext.getUserError() {
	fail(
		errorDb(
			repository = "user",
			violationCode = "get error",
			description = "Ошибка получения сотрудника"
		)
	)
}

class UserNotFoundException(message: String = "") : RuntimeException(message)
class ImageNotFoundException(message: String = "") : RuntimeException(message)