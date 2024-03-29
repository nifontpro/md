package ru.md.msc.rest.user.mappers

import ru.md.base_domain.model.BaseResponse
import ru.md.base_domain.model.baseResponse
import ru.md.msc.domain.user.biz.proc.UserContext
import ru.md.msc.domain.user.model.GenderCount
import ru.md.msc.domain.user.model.UserSettings
import ru.md.msc.rest.user.model.response.UserAwardResponse
import ru.md.msc.rest.user.model.response.UserDetailsDeptResponse
import ru.md.msc.rest.user.model.response.UserDetailsResponse
import ru.md.msc.rest.user.model.response.UserResponse

fun UserContext.toTransportUserDetails(): BaseResponse<UserDetailsResponse> {
	return baseResponse(userDetails.toUserDetailsResponse())
}

fun UserContext.toTransportUserDetailsDept(): BaseResponse<UserDetailsDeptResponse> {
	return baseResponse(
		UserDetailsDeptResponse(
			userDetails = userDetails.toUserDetailsResponse(),
			deptId = deptId
		)
	)
}

fun UserContext.toTransportUsersResponse(): BaseResponse<List<UserResponse>> {
//	return baseResponse(users.map { it.toUserResponseWithAwardAndActivity() })
	return baseResponse(users.map { it.toUserResponse() })
}

fun UserContext.toTransportUsersAwardsResponse(): BaseResponse<List<UserAwardResponse>> {
	return baseResponse(usersAwards.map { it.toUserAwardResponse() })
}

fun UserContext.toTransportGenderCount(): BaseResponse<GenderCount> {
	return baseResponse(genderCount)
}

fun UserContext.toTransportUserSettings(): BaseResponse<UserSettings> {
	return baseResponse(userSettings)
}

fun UserContext.toTransportHasOwner(): BaseResponse<Boolean> {
	return baseResponse(hasOwnerRole)
}

