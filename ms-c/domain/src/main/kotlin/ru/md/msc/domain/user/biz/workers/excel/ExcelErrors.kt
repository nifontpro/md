package ru.md.msc.domain.user.biz.workers.excel

import ru.md.base_domain.biz.helper.ContextError
import ru.md.base_domain.biz.helper.errorValidation
import ru.md.base_domain.biz.helper.fail
import ru.md.base_domain.biz.helper.otherError
import ru.md.msc.domain.user.biz.proc.UserContext

internal class ParseExcelException(message: String?) : RuntimeException(message)

internal fun UserContext.excelFormatError(message: String? = null) {
	fail(
		otherError(
			description = message ?: "Ошибка чтения Excel файла",
			field = "file",
			code = "excel-format error",
			level = ContextError.Levels.ERROR
		)
	)
}

internal fun parseDateError(field: String, date: String) = errorValidation(
	field = "date",
	violationCode = "not valid",
	description = "Событие: $field, неверный формат даты: $date",
	level = ContextError.Levels.WARNING
)