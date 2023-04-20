package ru.md.msc.domain.user.biz.proc

import ru.md.msc.domain.base.helper.ContextError
import ru.md.msc.domain.base.helper.errorDb
import ru.md.msc.domain.base.helper.fail

fun UserContext.userNotFound() {
	fail(
		errorDb(
			repository = "user",
			violationCode = "not found",
			description = "Сотрудник не найден"
		)
	)
}

fun UserContext.userImageNotFound() {
	fail(
		errorDb(
			repository = "user",
			violationCode = "image not found",
			description = "Изображение не найдено у сотрудника",
			level = ContextError.Levels.INFO,
		)
	)
}

fun UserContext.getUserError() {
	fail(
		errorDb(
			repository = "user",
			violationCode = "get error",
			description = "Ошибка чтения сотрудников"
		)
	)
}

class UserNotFoundException(message: String = "") : RuntimeException(message)
class ImageNotFoundException(message: String = "") : RuntimeException(message)