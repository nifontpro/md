package ru.md.msc.db.dept.model.mappers

import ru.md.msc.db.dept.model.DeptDetailsEntity
import ru.md.msc.domain.dept.model.DeptDetails

fun DeptDetailsEntity.toDeptDetails() = DeptDetails(
	dept = dept?.toDept(),
	address = address,
	email = email,
	phone = phone,
	description = description,
	createdAt = createdAt
)

fun DeptDetails.toDeptDetailsEntity() = DeptDetailsEntity(
	dept = dept?.toDeptEntity(),
	address = address,
	email = email,
	phone = phone,
	description = description,
	createdAt = createdAt
)