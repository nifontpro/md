package ru.md.msc.domain.dept.biz.proc

import ru.md.msc.domain.base.helper.errorDb
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

class DeptNotFoundException(message: String = "") : RuntimeException(message)