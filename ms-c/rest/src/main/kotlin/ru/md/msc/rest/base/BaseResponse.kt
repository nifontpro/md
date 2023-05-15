package ru.md.msc.rest.base

import ru.md.base_domain.model.PageInfo
import ru.md.msc.domain.base.biz.BaseClientContext
import ru.md.base_domain.biz.helper.ContextError
import ru.md.base_domain.biz.proc.ContextState

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

fun <T> BaseClientContext.baseResponse(data: T): BaseResponse<T> {
	return if (state == ContextState.FINISHING) {
		BaseResponse.success(data = data, pageInfo = pageInfo)
	} else {
		BaseResponse.error(errors = errors)
	}
}
