package ru.md.base_domain.dept.biz.errors

import ru.md.base_domain.biz.helper.errorDb
import ru.md.base_domain.biz.helper.errorUnauthorized
import ru.md.base_domain.biz.helper.fail
import ru.md.base_domain.biz.proc.BaseMedalsContext

fun BaseMedalsContext.deptNotFound() {
	fail(
		errorDb(
			repository = "dept",
			violationCode = "not found",
			description = "Отдел не найден"
		)
	)
}

fun BaseMedalsContext.rootDeptNotFound() {
	fail(
		errorDb(
			repository = "dept",
			violationCode = "root not found",
			description = "Не найден корневой отдел"
		)
	)
}

fun BaseMedalsContext.topLevelDeptNotFound() {
	fail(
		errorDb(
			repository = "dept",
			violationCode = "top level not found",
			description = "Отдел верхнего уровня не найден"
		)
	)
}

fun BaseMedalsContext.companyDeptNotFoundError() {
	fail(
		errorDb(
			repository = "dept",
			violationCode = "company id not found",
			description = "Id Компании не найден"
		)
	)
}

fun BaseMedalsContext.getDeptError() {
	fail(
		errorDb(
			repository = "dept",
			violationCode = "get error",
			description = "Ошибка чтения отделов"
		)
	)
}

fun BaseMedalsContext.getDeptAuthIOError() {
	fail(
		errorDb(
			repository = "dept",
			violationCode = "auth error",
			description = "Ошибка при проверки прав доступа к отделу"
		)
	)
}

fun BaseMedalsContext.deptAuthError() {
	fail(
		errorUnauthorized(
			role = "dept",
			message = "Нет доступа к отделу или его данным",
		)
	)
}

fun BaseMedalsContext.deptDbError() {
	fail(
		errorDb(
			repository = "dept",
			violationCode = "i/o",
			description = "Ошибка чтения или записи"
		)
	)
}

class RootDeptNotFoundException(message: String = "") : RuntimeException(message)
class CompanyDeptNotFoundException(message: String = "") : RuntimeException(message)
class DeptNotFoundException(message: String = "") : RuntimeException(message)
class DeptIOException(message: String = "") : RuntimeException(message)
class TopLevelDeptNotFoundException(message: String = "") : RuntimeException(message)