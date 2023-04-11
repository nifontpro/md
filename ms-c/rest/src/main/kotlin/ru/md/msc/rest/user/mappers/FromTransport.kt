package ru.md.msc.rest.user.mappers

import ru.md.msc.domain.user.biz.proc.UserCommand
import ru.md.msc.domain.user.biz.proc.UserContext
import ru.md.msc.domain.user.model.User
import ru.md.msc.domain.user.model.UserDetails
import ru.md.msc.rest.user.request.CreateOwnerRequest

fun UserContext.fromTransport(request: CreateOwnerRequest) {
	command = UserCommand.CREATE_OWNER
	emailVerified = request.emailVerified
	user = User(
		email = request.email,
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