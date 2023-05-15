package ru.md.msc.rest.user.mappers

import ru.md.msc.domain.user.biz.proc.UserContext
import ru.md.msc.domain.user.model.User
import ru.md.base_rest.model.BaseResponse
import ru.md.base_rest.model.baseResponse
import ru.md.msc.rest.user.model.response.UserDetailsResponse

fun UserContext.toTransportUserDetails(): BaseResponse<UserDetailsResponse> {
	return baseResponse(userDetails.toUserDetailsResponse())
}

fun UserContext.toTransportUsers(): BaseResponse<List<User>> {
	return baseResponse(users)
}
