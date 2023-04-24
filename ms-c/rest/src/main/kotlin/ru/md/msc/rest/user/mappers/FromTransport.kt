package ru.md.msc.rest.user.mappers

import ru.md.msc.domain.dept.model.Dept
import ru.md.msc.domain.user.biz.proc.UserCommand
import ru.md.msc.domain.user.biz.proc.UserContext
import ru.md.msc.domain.user.model.User
import ru.md.msc.domain.user.model.UserDetails
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
		description = request.description
	)
}

fun UserContext.fromTransport(request: UpdateUserRequest) {
	command = UserCommand.UPDATE
	authId = request.authId
	userId = request.userId // auth
	user = User(
		id = userId,
		dept = Dept(id = deptId),
		authEmail = request.authEmail,
		firstname = request.firstname,
		patronymic = request.patronymic,
		lastname = request.lastname,
		post = request.post,
		gender = request.gender,
//		roles = request.roles
	)
	userDetails = UserDetails(
		user = user,
		phone = request.phone,
		address = request.address,
		description = request.description
	)
}

@Suppress("UNUSED_PARAMETER")
fun UserContext.fromTransport(request: GetProfilesRequest) {
	command = UserCommand.GET_PROFILES
}

fun UserContext.fromTransport(request: GetUsersByDeptRequest) {
	command = UserCommand.GET_BY_DEPT
	authId = request.authId
	deptId = request.deptId
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
}

fun UserContext.fromTransport(request: DeleteUserImageRequest) {
	command = UserCommand.IMG_DELETE
	imageId = request.imageId
	userId = request.userId
}