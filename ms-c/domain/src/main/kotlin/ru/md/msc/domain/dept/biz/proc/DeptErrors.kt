package ru.md.msc.domain.dept.biz.proc

import ru.md.base_domain.biz.helper.errorDb
import ru.md.base_domain.biz.helper.errorUnauthorized
import ru.md.base_domain.biz.helper.fail
import ru.md.msc.domain.base.biz.BaseClientContext

fun BaseClientContext.deptNotFound() {
	fail(
		errorDb(
			repository = "dept",
			violationCode = "not found",
			description = "Отдел не найден"
		)
	)
}

fun BaseClientContext.rootDeptNotFound() {
	fail(
		errorDb(
			repository = "dept",
			violationCode = "root not found",
			description = "Не найден корневой отдел"
		)
	)
}

fun BaseClientContext.topLevelDeptNotFound() {
	fail(
		errorDb(
			repository = "dept",
			violationCode = "top level not found",
			description = "Отдел верхнего уровня не найден"
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

class RootDeptNotFoundException(message: String = "") : RuntimeException(message)
class DeptNotFoundException(message: String = "") : RuntimeException(message)
class TopLevelDeptNotFoundException(message: String = "") : RuntimeException(message)