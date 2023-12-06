package ru.md.msc.rest.user.mappers

import ru.md.base_domain.model.BaseQuery
import ru.md.base_rest.model.mapper.toBaseQuery
import ru.md.base_domain.dept.model.Dept
import ru.md.base_domain.model.mappers.toLocalDateTimeUTC
import ru.md.msc.domain.user.biz.proc.UserCommand
import ru.md.msc.domain.user.biz.proc.UserContext
import ru.md.base_domain.user.model.User
import ru.md.msc.domain.user.model.UserDetails
import ru.md.msc.domain.user.model.UserSettings
import ru.md.msc.rest.user.model.request.*

fun UserContext.fromTransport(request: CreateOwnerRequest) {
	command = UserCommand.CREATE_OWNER
	user = User(
		firstname = request.firstname,
		patronymic = request.patronymic,
		lastname = request.lastname,
		post = request.post,
		gender = request.gender,
	)
	userDetails = UserDetails(
		user = user,
		phone = request.phone,
		address = request.address,
		description = request.description
	)
}

fun UserContext.fromTransport(request: CreateUserRequest) {
	command = UserCommand.CREATE
	authId = request.authId
	deptId = request.deptId // auth
	user = User(
		dept = Dept(id = deptId),
		authEmail = request.authEmail,
		firstname = request.firstname,
		patronymic = request.patronymic,
		lastname = request.lastname,
		post = request.post,
		gender = request.gender,
		roles = request.roles
	)
	userDetails = UserDetails(
		user = user,
		phone = request.phone,
		address = request.address,
		description = request.description,
		schedule = request.schedule,
	)
}

fun UserContext.fromTransport(request: UpdateUserRequest) {
	command = UserCommand.UPDATE
	authId = request.authId
	userId = request.userId // auth
	user = User(
		id = userId,
		dept = Dept(id = request.deptId),
		authEmail = request.authEmail,
		firstname = request.firstname,
		patronymic = request.patronymic,
		lastname = request.lastname,
		post = request.post,
		gender = request.gender,
		roles = request.roles
	)
	userDetails = UserDetails(
		user = user,
		phone = request.phone,
		address = request.address,
		description = request.description,
		schedule = request.schedule,
	)
	birthDate = request.birthDate?.toLocalDateTimeUTC()
	jobDate = request.jobDate?.toLocalDateTimeUTC()
}

@Suppress("UNUSED_PARAMETER")
fun UserContext.fromTransport(request: GetProfilesRequest) {
	command = UserCommand.GET_PROFILES
}

fun UserContext.fromTransport(request: GetUsersBySubDeptsRequest) {
	command = UserCommand.GET_BY_SUB_DEPTS
	authId = request.authId
	deptId = request.deptId
	baseQuery = request.baseRequest?.toBaseQuery() ?: BaseQuery()
}

fun UserContext.fromTransport(request: GetUsersByDeptsExcludeRequest) {
	command = UserCommand.GET_BY_SUB_DEPTS_EXCLUDE_AWARD
	authId = request.authId
	deptId = request.deptId
	awardId = request.awardId
	actionType = request.actionType
	baseQuery = request.baseRequest?.toBaseQuery() ?: BaseQuery()
}

fun UserContext.fromTransport(request: GetUserByIdRequest) {
	command = UserCommand.GET_BY_ID_DETAILS
	authId = request.authId
	userId = request.userId
}

fun UserContext.fromTransport(request: DeleteUserRequest) {
	command = UserCommand.DELETE
	authId = request.authId
	userId = request.userId
	deleteForever = request.forever
}

fun UserContext.fromTransport(request: DeleteUserImageRequest) {
	command = UserCommand.IMG_DELETE
	imageId = request.imageId
	authId = request.authId
	userId = request.userId
}

fun UserContext.fromTransport(request: GetGenderCountByDeptRequest) {
	command = UserCommand.GENDER_COUNT_BY_DEPTS
	authId = request.authId
	deptId = request.deptId
	baseQuery = request.baseRequest?.toBaseQuery() ?: BaseQuery()
}

fun UserContext.fromTransport(request: GetUsersWithActivityRequest) {
	command = UserCommand.GET_WITH_ACTIVITY
	authId = request.authId
	deptId = request.deptId
	baseQuery = request.baseRequest?.toBaseQuery() ?: BaseQuery()
}

fun UserContext.fromTransport(request: GetUsersWithAwardsRequest) {
	command = UserCommand.GET_WITH_AWARDS
	authId = request.authId
	deptId = request.deptId
	baseQuery = request.baseRequest?.toBaseQuery() ?: BaseQuery()
}

fun UserContext.fromTransport(request: GetUsersWithAwardCountRequest) {
	command = UserCommand.GET_WITH_AWARD_COUNT
	authId = request.authId
	deptId = request.deptId
	baseQuery = request.baseRequest?.toBaseQuery() ?: BaseQuery()
}

fun UserContext.fromTransport(request: SaveUserSettingsRequest) {
	command = UserCommand.SAVE_SETTINGS
	userId = request.userId
	authId = userId
	userSettings = UserSettings(
		userId = userId,
		showOnboarding = request.showOnboarding,
		pageOnboarding = request.pageOnboarding
	)
}

fun UserContext.fromTransport(request: GetUserSettingsRequest) {
	command = UserCommand.GET_SETTINGS
	userId = request.userId
	authId = userId
}

fun UserContext.fromTransport(request: HasUserOwnerRequest) {
	command = UserCommand.CHECK_HAS_OWNER_ROLE
	userId = request.userId
}

/**
 * Service
 */
@Suppress("UNUSED_PARAMETER")
fun UserContext.fromTransport(request: SetMainUserImagesRequest) {
	command = UserCommand.SRV_SET_MAIN_IMG
}

@Suppress("UNUSED_PARAMETER")
fun UserContext.fromTransport(request: AddGenderRequest) {
	command = UserCommand.SRV_ADD_NAMES_DB
}


