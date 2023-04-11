package ru.md.msc.rest.user.mappers

import ru.md.base.dom.biz.BaseContext
import ru.md.base.dom.biz.ContextState
import ru.md.msc.domain.user.biz.proc.UserContext
import ru.md.msc.domain.user.model.UserDetails
import ru.md.msc.rest.base.BaseResponse

fun UserContext.toTransportGetUserDetails(): BaseResponse<UserDetails> = baseResponse(userDetails)

private fun <T> BaseContext.baseResponse(data: T): BaseResponse<T> {
	return if (state == ContextState.FINISHING) {
		BaseResponse.success(data = data)
	} else {
		BaseResponse.error(errors = errors)
	}
}

