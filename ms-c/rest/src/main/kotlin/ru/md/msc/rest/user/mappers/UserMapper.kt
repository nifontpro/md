package ru.md.msc.rest.user.mappers

import ru.md.base_rest.model.mapper.toBaseImageResponse
import ru.md.base_domain.user.model.User
import ru.md.msc.domain.user.model.UserAward
import ru.md.msc.rest.award.mappers.toActivityResponse
import ru.md.msc.rest.award.mappers.toAwardResponse
import ru.md.msc.rest.user.model.response.UserAwardResponse
import ru.md.msc.rest.user.model.response.UserResponse

fun UserAward.toUserAwardResponse() = UserAwardResponse(
	id = id,
	dept = dept,
	firstname = firstname,
	patronymic = patronymic,
	lastname = lastname,
	authEmail = authEmail,
	gender = gender,
	post = post,
	roles = roles,
	images = images.map { it.toBaseImageResponse() },
	awardCount = awardCount,
	scores = scores,
	mainImg = mainImg,
	archive = archive,
	activities = activities.map { it.toActivityResponse() },
	awards = awards.map { it.toAwardResponse() },
)

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
	images = images.map { it.toBaseImageResponse() },
	awardCount = awardCount,
	scores = scores,
	mainImg = mainImg,
	archive = archive,
)