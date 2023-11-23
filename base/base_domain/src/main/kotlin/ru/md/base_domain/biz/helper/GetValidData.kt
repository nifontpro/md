package ru.md.base_domain.biz.helper

import ru.md.base_domain.biz.proc.BaseContext
import ru.md.base_domain.biz.proc.ContextState
import ru.md.base_domain.model.BaseResponse

suspend fun <R> BaseContext.getBaseData(
	body: suspend () -> BaseResponse<R>
): R? {
	val res = body()
	return if (res.success) {
		res.data
	} else {
		errors.addAll(res.errors)
		state = ContextState.FAILING
		null
	}
}