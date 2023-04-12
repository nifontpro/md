package ru.md.msc.domain.base.model

import ru.md.msc.domain.base.biz.BaseContext
import ru.md.msc.domain.base.biz.ContextState
import ru.md.msc.domain.base.helper.addError
import ru.md.msc.domain.base.helper.errorDb

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