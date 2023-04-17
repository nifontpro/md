package ru.md.msc.db.user.model.mappers

import ru.md.msc.db.dept.model.mappers.toDept
import ru.md.msc.db.dept.model.mappers.toDeptEntity
import ru.md.msc.db.user.model.UserEntity
import ru.md.msc.domain.dept.model.Dept
import ru.md.msc.domain.user.model.User

fun User.toUserEntity() = UserEntity(
	id = id,
	dept = dept?.toDeptEntity(),
	authEmail = authEmail,
	firstname = firstname,
	patronymic = patronymic,
	lastname = lastname,
	gender = gender,
	post = post,
)

// Для ленивой загрузки без отделов
fun UserEntity.toUser() = User(
	id = id,
	dept = Dept(id = dept?.id),
	authEmail = authEmail,
	firstname = firstname,
	patronymic = patronymic,
	lastname = lastname,
	gender = gender,
	post = post,
	roles = roles.map { it.roleUser }.toSet(),
)

fun UserEntity.toUserDept() = User(
	id = id,
	dept = dept?.toDept(),
	authEmail = authEmail,
	firstname = firstname,
	patronymic = patronymic,
	lastname = lastname,
	gender = gender,
	post = post,
	roles = roles.map { it.roleUser }.toSet(),
)