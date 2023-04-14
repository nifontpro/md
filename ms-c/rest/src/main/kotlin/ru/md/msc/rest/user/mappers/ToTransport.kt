package ru.md.msc.rest.user.mappers

import ru.md.msc.domain.user.biz.proc.UserContext
import ru.md.msc.domain.user.model.User
import ru.md.msc.rest.base.BaseResponse
import ru.md.msc.rest.base.baseResponse
import ru.md.msc.rest.user.model.response.UserDetailsResponse

fun UserContext.toTransportGetUserDetails(): BaseResponse<UserDetailsResponse> {
	return baseResponse(userDetails.toUserDetailsResponse())
}

fun UserContext.toTransportGetUsers(): BaseResponse<List<User>> {
	return baseResponse(users)
}
