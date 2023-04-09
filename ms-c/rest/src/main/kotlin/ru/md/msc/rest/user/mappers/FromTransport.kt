package ru.md.msc.rest.user.mappers

import ru.md.msc.domain.user.biz.proc.UserCommand
import ru.md.msc.domain.user.biz.proc.UserContext
import ru.md.msc.domain.user.model.User
import ru.md.msc.rest.user.request.CreateOwnerRequest

fun UserContext.fromTransport(request: CreateOwnerRequest) {
	command = UserCommand.CREATE_OWNER
	user = User(
		email = request.email,
		firstname = request.firstname,
		patronymic = request.patronymic,
		lastname = request.lastname,
	)
}