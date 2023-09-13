package ru.md.msc.rest.user.mappers

import ru.md.msc.domain.user.model.User
import ru.md.msc.rest.award.mappers.toActivityResponse
import ru.md.msc.rest.award.mappers.toAwardResponse
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
	awardCount = awardCount,
	scores = scores,
	mainImg = mainImg,
	isDelete = archive,
	activities = activities.map { it.toActivityResponse() },
	awards = awards.map { it.toAwardResponse() },
)