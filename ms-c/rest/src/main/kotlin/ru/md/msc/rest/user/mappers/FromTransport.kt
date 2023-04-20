package ru.md.msc.rest.user.mappers

import ru.md.msc.domain.image.model.BaseImage
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
	baseImage = BaseImage(id = request.imageId)
	userId = request.userId
}