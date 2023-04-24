package ru.md.msc.domain.dept.biz.proc

import ru.md.msc.domain.base.biz.BaseContext
import ru.md.msc.domain.base.helper.errorDb
import ru.md.msc.domain.base.helper.errorUnauthorized
import ru.md.msc.domain.base.helper.fail

fun DeptContext.deptNotFound() {
	fail(
		errorDb(
			repository = "dept",
			violationCode = "not found",
			description = "Отдел не найден"
		)
	)
}

fun DeptContext.getDeptError() {
	fail(
		errorDb(
			repository = "dept",
			violationCode = "get error",
			description = "Ошибка чтения отделов"
		)
	)
}

fun BaseContext.getDeptAuthIOError() {
	fail(
		errorDb(
			repository = "dept",
			violationCode = "auth error",
			description = "Ошибка при проверки прав доступа к отделу"
		)
	)
}

fun BaseContext.deptAuthError() {
	fail(
		errorUnauthorized(
			role = "deptId",
			message = "Нет доступа к отделу",
		)
	)
}

class DeptNotFoundException(message: String = "") : RuntimeException(message)