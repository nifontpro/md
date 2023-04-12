package ru.md.msc.rest.base

import ru.md.msc.domain.base.biz.BaseContext
import ru.md.msc.domain.base.biz.ContextState
import ru.md.msc.domain.base.helper.ContextError

data class BaseResponse<out T>(
	val data: T? = null,
	val success: Boolean = true,
	val errors: List<ContextError> = emptyList()
) {
	companion object {
		fun <T> success(data: T? = null) = BaseResponse(data = data, success = true)
		fun error(errors: List<ContextError>) = BaseResponse(data = null, success = false, errors = errors)
	}
}

fun <T> BaseContext.baseResponse(data: T): BaseResponse<T> {
	return if (state == ContextState.FINISHING) {
		BaseResponse.success(data = data)
	} else {
		BaseResponse.error(errors = errors)
	}
}
