package ru.md.msc.domain.user.biz.proc

import ru.md.msc.domain.base.biz.BaseClientContext
import ru.md.base_domain.biz.helper.errorDb
import ru.md.base_domain.biz.helper.fail

fun userCreateErrorExt() = errorDb(
	repository = "user",
	violationCode = "create",
	description = "Ошибка создания профиля сотрудника"
)

fun BaseClientContext.userCreateError() {
	fail(
		userCreateErrorExt()
	)
}

fun userUpdateErrorExt() = errorDb(
	repository = "user",
	violationCode = "update",
	description = "Ошибка обновления профиля сотрудника"
)

fun BaseClientContext.userUpdateError() {
	fail(
		userUpdateErrorExt()
	)
}

fun userNotFoundErrorExt() = errorDb(
	repository = "user",
	violationCode = "not found",
	description = "Сотрудник не найден"
)

fun BaseClientContext.userNotFoundError() {
	fail(
		userNotFoundErrorExt()
	)
}

fun BaseClientContext.getUserError() {
	fail(
		getUserErrorExt()
	)
}

fun getUserErrorExt() = errorDb(
	repository = "user",
	violationCode = "get error",
	description = "Ошибка получения профиля сотрудника"
)

fun UserContext.getUserCountError() {
	fail(
		errorDb(
			repository = "user",
			violationCode = "count",
			description = "Ошибка подсчета сотрудников"
		)
	)
}

class UserNotFoundException(message: String = "") : RuntimeException(message)