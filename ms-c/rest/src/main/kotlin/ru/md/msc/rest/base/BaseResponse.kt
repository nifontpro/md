package ru.md.msc.rest.base

import ru.md.msc.domain.base.biz.BaseContext
import ru.md.msc.domain.base.biz.ContextState
import ru.md.msc.domain.base.helper.ContextError
import ru.md.msc.domain.base.model.PageInfo

data class BaseResponse<out T>(
	val data: T? = null,
	val success: Boolean = true,
	val errors: List<ContextError> = emptyList(),
	val pageInfo: PageInfo? = null,
) {
	companion object {
		fun <T> success(data: T? = null, pageInfo: PageInfo?) =
			BaseResponse(data = data, success = true, pageInfo = pageInfo)

		fun error(errors: List<ContextError>) = BaseResponse(data = null, success = false, errors = errors)
	}
}

fun <T> BaseContext.baseResponse(data: T): BaseResponse<T> {
	return if (state == ContextState.FINISHING) {
		BaseResponse.success(data = data, pageInfo = pageInfo)
	} else {
		BaseResponse.error(errors = errors)
	}
}
