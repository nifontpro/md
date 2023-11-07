package ru.md.msc.db.dept.model.mappers

import ru.md.base_db.dept.model.mappers.toDept
import ru.md.base_db.dept.model.mappers.toDeptEntity
import ru.md.base_db.image.mappers.toBaseImage
import ru.md.msc.db.dept.model.DeptDetailsEntity
import ru.md.msc.domain.dept.model.DeptDetails
import java.time.LocalDateTime

fun DeptDetailsEntity.toDeptDetails() = DeptDetails(
	dept = dept.toDept(),
	address = address,
	email = email,
	phone = phone,
	description = description,
	createdAt = createdAt,
	images = images.map { it.toBaseImage() }
)

fun DeptDetails.toDeptDetailsEntity(create: Boolean = false) = DeptDetailsEntity(
	dept = dept.toDeptEntity(create = create),
	address = address,
	email = email,
	phone = phone,
	description = description,
	createdAt = if (create) LocalDateTime.now() else createdAt,
)