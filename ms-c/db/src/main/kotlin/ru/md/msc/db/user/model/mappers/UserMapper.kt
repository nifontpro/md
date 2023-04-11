package ru.md.msc.db.user.model.mappers

import ru.md.msc.db.user.model.UserEntity
import ru.md.msc.domain.user.model.Gender
import ru.md.msc.domain.user.model.RoleEnum
import ru.md.msc.domain.user.model.User

fun User.toUserEntity() = UserEntity(
	id = id,
	email = email,
	firstname = firstname,
	patronymic = patronymic,
	lastname = lastname,
	gender = gender ?: Gender.UNDEF,
//	roles = roles.map { it.toRoleEntity() }.toSet(),
	post = post,
)

fun UserEntity.toUser() = User(
	id = id,
	email = email,
	firstname = firstname,
	patronymic = patronymic,
	lastname = lastname,
	gender = gender,
	roles = roles.map { it.roleEnum }.toSet(),
	post = post,
)