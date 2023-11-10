package ru.md.base_db.user.model.mappers

import ru.md.base_db.dept.model.mappers.toDept
import ru.md.base_db.dept.model.mappers.toDeptEntity
import ru.md.base_db.dept.model.mappers.toDeptWithCompany
import ru.md.base_db.user.model.UserEntity
import ru.md.base_domain.dept.model.Dept
import ru.md.base_domain.user.model.User

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
	normImg = normImg,
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
	normImg = normImg,
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
	normImg = normImg,
	archive = archive,
)

fun UserEntity.toUserWithDeptCompanyOnly() = User(
	id = id ?: 0,
	dept = dept?.toDeptWithCompany(),
	authEmail = authEmail,
	firstname = firstname,
	patronymic = patronymic,
	lastname = lastname,
	gender = gender,
	post = post,
	mainImg = mainImg,
	normImg = normImg,
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
	normImg = normImg,
	archive = archive,
	roles = roles.map { it.roleUser }.toSet(),
)