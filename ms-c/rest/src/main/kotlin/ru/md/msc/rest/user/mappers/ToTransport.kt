package ru.md.msc.rest.user.mappers

import ru.md.base_domain.rest.BaseResponse
import ru.md.base_domain.rest.baseResponse
import ru.md.msc.domain.user.biz.proc.UserContext
import ru.md.msc.domain.user.model.GenderCount
import ru.md.msc.domain.user.model.User
import ru.md.msc.rest.user.model.response.UserDetailsResponse
import ru.md.msc.rest.user.model.response.UserResponse

fun UserContext.toTransportUserDetails(): BaseResponse<UserDetailsResponse> {
	return baseResponse(userDetails.toUserDetailsResponse())
}

fun UserContext.toTransportUsers(): BaseResponse<List<User>> {
	return baseResponse(users)
}

fun UserContext.toTransportUsersResponse(): BaseResponse<List<UserResponse>> {
	return baseResponse(users.map { it.toUserResponse() })
}

fun UserContext.toTransportGenderCount(): BaseResponse<GenderCount> {
	return baseResponse(genderCount)
}
