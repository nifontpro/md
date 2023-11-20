package ru.md.msc.domain.user.biz.proc

import ru.md.base_domain.biz.helper.errorDb
import ru.md.base_domain.biz.helper.fail
import ru.md.msc.domain.base.biz.BaseClientContext

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

fun UserContext.getUserCountError() {
	fail(
		errorDb(
			repository = "user",
			violationCode = "count",
			description = "Ошибка подсчета сотрудников"
		)
	)
}

fun UserContext.userDbError() {
	fail(
		errorDb(
			repository = "user",
			violationCode = "i/o",
			description = "Ошибка чтения или записи"
		)
	)
}

fun UserContext.userEventError() {
	fail(
		errorDb(
			repository = "user",
			violationCode = "event i/o",
			description = "Ошибка чтения или записи событий Сотрудника"
		)
	)
}

class UserIOException(message: String = "") : RuntimeException(message)

