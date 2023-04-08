package ru.md.msc.db.user.model.mappers

import ru.md.msc.db.user.model.UserEntity
import ru.md.msc.domain.user.model.RoleEnum
import ru.md.msc.domain.user.model.User

fun User.toUserEntity() = UserEntity(
	id = id,
	email = email,
	firstname = firstname,
	patronymic = patronymic,
	lastname = lastname,
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
	roles = roles.map { roleCodeToEnum(it.code) }.toSet(),
	post = post,
)

private fun roleCodeToEnum(code: String): RoleEnum {
	return RoleEnum.values().find {
		it.code == code
	} ?: throw Exception("Ошибка конвертации кода роли: $code")
}