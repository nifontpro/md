package ru.md.base.dom.helper

import ru.md.base.dom.biz.BaseContext
import ru.md.base.dom.biz.ContextState

fun BaseContext.addError(error: ContextError) = errors.add(error)

fun BaseContext.fail(error: ContextError) {
	addError(error)
	state = ContextState.FAILING
}

fun errorValidation(
	field: String,
	/**
	 * Код, характеризующий ошибку. Не должен включать имя поля или указание на валидацию.
	 * Например: empty, badSymbols, tooLong, etc
	 */
	violationCode: String,
	description: String,
	level: ContextError.Levels = ContextError.Levels.INFO,
) = ContextError(
	code = "validation-$field-$violationCode",
	field = field,
	group = "validation",
	message = description,
	level = level,
)

fun errorDb(
	repository: String,
	violationCode: String,
	description: String,
	level: ContextError.Levels = ContextError.Levels.ERROR,
) = ContextError(
	code = "db-$repository-$violationCode",
	field = repository,
	group = "db",
	message = "Ошибка БД: $description",
	level = level,
)

fun errorUnauthorized(
	role: String, // Уровень доступа
	description: String,
	level: ContextError.Levels = ContextError.Levels.UNAUTHORIZED,
) = ContextError(
	code = "unauthorized-$role",
	field = role,
	group = "unauthorized",
	message = "Недостаточно прав для совершения операции (минимум '$description')",
	level = level,
)

fun otherError(
	description: String,
	field: String,
	code: String = "other",
	level: ContextError.Levels = ContextError.Levels.INFO,
) = ContextError(
	code = code,
	field = field,
	group = "other",
	message = description,
	level = level,
)