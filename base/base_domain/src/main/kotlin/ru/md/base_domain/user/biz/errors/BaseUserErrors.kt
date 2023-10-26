package ru.md.base_domain.user.biz.errors

import ru.md.base_domain.biz.helper.errorDb
import ru.md.base_domain.biz.helper.fail
import ru.md.base_domain.biz.proc.BaseMedalsContext


fun userNotFoundErrorExt() = errorDb(
	repository = "user",
	violationCode = "not found",
	description = "Сотрудник не найден"
)

fun BaseMedalsContext.userNotFoundError() {
	fail(
		userNotFoundErrorExt()
	)
}

fun BaseMedalsContext.getUserError() {
	fail(
		getUserErrorExt()
	)
}

fun getUserErrorExt() = errorDb(
	repository = "user",
	violationCode = "get error",
	description = "Ошибка получения профиля сотрудника"
)

class UserNotFoundException(message: String = "") : RuntimeException(message)