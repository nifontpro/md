package ru.md.msc.rest.user.mappers

import ru.md.msc.domain.user.model.User
import ru.md.msc.rest.award.mappers.toActivityResponse
import ru.md.msc.rest.user.model.response.UserResponse

fun User.toUserResponse() = UserResponse(
	id = id,
	dept = dept,
	firstname = firstname,
	patronymic = patronymic,
	lastname = lastname,
	authEmail = authEmail,
	gender = gender,
	post = post,
	roles = roles,
	images = images,
	activities = activities.map { it.toActivityResponse() }
)