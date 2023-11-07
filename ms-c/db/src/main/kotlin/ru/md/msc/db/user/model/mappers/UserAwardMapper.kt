package ru.md.msc.db.user.model.mappers

import ru.md.base_db.dept.model.mappers.toDept
import ru.md.base_db.image.mappers.toBaseImage
import ru.md.msc.db.award.model.mapper.toActivityOnlyAward
import ru.md.msc.db.award.model.mapper.toAwardLazy
import ru.md.msc.db.user.model.UserAwardEntity
import ru.md.msc.domain.user.model.UserAward

fun UserAwardEntity.toUserAward() = UserAward(
	id = id ?: 0,
	dept = dept?.toDept(),
	authEmail = authEmail,
	firstname = firstname,
	patronymic = patronymic,
	lastname = lastname,
	gender = gender,
	post = post,
	mainImg = mainImg,
	archive = archive,
	images = images.map { it.toBaseImage() },
	awards = awards.map { it.toAwardLazy() }
)

fun UserAwardEntity.toUserActivity() = UserAward(
	id = id ?: 0,
	dept = dept?.toDept(),
	authEmail = authEmail,
	firstname = firstname,
	patronymic = patronymic,
	lastname = lastname,
	gender = gender,
	post = post,
	mainImg = mainImg,
	archive = archive,
	images = images.map { it.toBaseImage() },
	activities = activities.map { it.toActivityOnlyAward() },
)