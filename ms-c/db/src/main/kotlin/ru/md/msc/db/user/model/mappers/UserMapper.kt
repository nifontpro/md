package ru.md.msc.db.user.model.mappers

import ru.md.msc.db.base.mapper.toImage
import ru.md.msc.db.dept.model.mappers.toDept
import ru.md.msc.db.dept.model.mappers.toDeptEntity
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

// Для ленивой загрузки без отделов
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