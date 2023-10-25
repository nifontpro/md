package ru.md.msc.db.user.model.mappers

import ru.md.base_db.mapper.toBaseImage
import ru.md.msc.db.award.model.mapper.toActivityOnlyAward
import ru.md.msc.db.award.model.mapper.toAwardLazy
import ru.md.msc.db.dept.model.mappers.toDept
import ru.md.msc.db.dept.model.mappers.toDeptEntity
import ru.md.msc.db.dept.model.mappers.toDeptLazy
import ru.md.msc.db.user.model.UserAwardEntity
import ru.md.msc.db.user.model.UserEntity
import ru.md.msc.domain.dept.model.Dept
import ru.md.msc.domain.user.model.User
import ru.md.msc.domain.user.model.UserAward

fun User.toUserEntity(create: Boolean = false) = UserEntity(
	id = if (create) null else id,
	dept = dept?.toDeptEntity(),
	authEmail = authEmail,
	firstname = firstname,
	patronymic = patronymic,
	lastname = lastname,
	gender = gender,
	post = post,
)

// Для ленивой загрузки только с ролями
fun UserEntity.toUserOnlyRoles() = User(
	id = id ?: 0,
	dept = Dept(id = dept?.id ?: 0),
	authEmail = authEmail,
	firstname = firstname,
	patronymic = patronymic,
	lastname = lastname,
	gender = gender,
	post = post,
	mainImg = mainImg,
	archive = archive,
	roles = roles.map { it.roleUser }.toSet(),
)

// Для ленивой загрузки без отделов и ролей
fun UserEntity.toUserLazy() = User(
	id = id ?: 0,
	dept = Dept(id = dept?.id ?: 0),
	authEmail = authEmail,
	firstname = firstname,
	patronymic = patronymic,
	lastname = lastname,
	gender = gender,
	post = post,
	mainImg = mainImg,
	archive = archive,
)

fun UserEntity.toUserWithDeptOnly() = User(
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
)

fun UserEntity.toUser() = User(
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
	roles = roles.map { it.roleUser }.toSet(),
	images = images.map { it.toBaseImage() }
)

fun UserAwardEntity.toUserAward() = UserAward(
	id = id ?: 0,
	dept = dept?.toDeptLazy(),
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
	dept = dept?.toDeptLazy(),
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