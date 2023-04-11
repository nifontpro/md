package ru.md.base.dom.model

import ru.md.base.dom.biz.BaseContext
import ru.md.base.dom.biz.ContextState
import ru.md.base.dom.helper.addError
import ru.md.base.dom.helper.errorDb

suspend fun <T> BaseContext.checkRepositoryData(operation: suspend () -> RepositoryData<T>): T? {
	val result = operation()
	return if (result.success) {
		result.data
	} else {
		addError(
			errorDb(
				repository = result.error?.repository ?: "undefined repository",
				violationCode = result.error?.violationCode ?: "error",
				description = result.error?.description ?: "Внутренняя ошибка БД"
			)
		)
		state = ContextState.FAILING
		null
	}
}