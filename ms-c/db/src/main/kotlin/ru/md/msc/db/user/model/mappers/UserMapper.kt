package ru.md.msc.db.user.model.mappers

import ru.md.base_db.mapper.toImage
import ru.md.msc.db.award.model.mapper.toActivityOnlyAward
import ru.md.msc.db.award.model.mapper.toAwardOnlyImages
import ru.md.msc.db.dept.model.mappers.toDept
import ru.md.msc.db.dept.model.mappers.toDeptEntity
import ru.md.msc.db.dept.model.mappers.toDeptOnlyName
import ru.md.msc.db.user.model.UserEntity
import ru.md.msc.domain.dept.model.Dept
import ru.md.msc.domain.user.model.User

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
	roles = roles.map { it.roleUser }.toSet(),
	images = images.map { it.toImage() }
)

fun UserEntity.toUserActivity() = User(
	id = id ?: 0,
	dept = dept?.toDeptOnlyName(),
	authEmail = authEmail,
	firstname = firstname,
	patronymic = patronymic,
	lastname = lastname,
	gender = gender,
	post = post,
	images = images.map { it.toImage() },
	activities = activities.map { it.toActivityOnlyAward() }
)

fun UserEntity.toUserAward() = User(
	id = id ?: 0,
	dept = dept?.toDeptOnlyName(),
	authEmail = authEmail,
	firstname = firstname,
	patronymic = patronymic,
	lastname = lastname,
	gender = gender,
	post = post,
	images = images.map { it.toImage() },
	awards = awards.map { it.toAwardOnlyImages() }
)

fun UserEntity.toUserWithoutDept() = User(
	id = id ?: 0,
	authEmail = authEmail,
	firstname = firstname,
	patronymic = patronymic,
	lastname = lastname,
	gender = gender,
	post = post,
	roles = roles.map { it.roleUser }.toSet(),
	images = images.map { it.toImage() }
)