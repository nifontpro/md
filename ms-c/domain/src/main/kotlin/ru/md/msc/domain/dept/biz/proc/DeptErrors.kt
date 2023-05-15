package ru.md.msc.domain.dept.biz.proc

import ru.md.msc.domain.base.biz.BaseClientContext
import ru.md.base_domain.biz.helper.errorDb
import ru.md.base_domain.biz.helper.errorUnauthorized
import ru.md.base_domain.biz.helper.fail

fun BaseClientContext.deptNotFound() {
	fail(
		errorDb(
			repository = "dept",
			violationCode = "not found",
			description = "Отдел не найден"
		)
	)
}

fun BaseClientContext.getDeptError() {
	fail(
		errorDb(
			repository = "dept",
			violationCode = "get error",
			description = "Ошибка чтения отделов"
		)
	)
}

fun BaseClientContext.getDeptAuthIOError() {
	fail(
		errorDb(
			repository = "dept",
			violationCode = "auth error",
			description = "Ошибка при проверки прав доступа к отделу"
		)
	)
}

fun BaseClientContext.deptAuthError() {
	fail(
		errorUnauthorized(
			role = "dept",
			message = "Нет доступа к отделу или его данным",
		)
	)
}

class DeptNotFoundException(message: String = "") : RuntimeException(message)