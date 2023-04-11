package ru.md.msc.rest.base

import ru.md.base.dom.helper.ContextError

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