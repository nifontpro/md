package ru.md.msc.db.dept.model.mappers

import ru.md.base_db.mapper.toImage
import ru.md.msc.db.dept.model.DeptEntity
import ru.md.msc.domain.dept.model.Dept

fun DeptEntity.toDept() = Dept(
	id = id ?: 0,
	parentId = parentId,
	name = name,
	classname = classname,
	type = type,
	images = images.map { it.toImage() }
)

fun Dept.toDeptEntity(create: Boolean = false) = DeptEntity(
	id = if (create) null else id,
	parentId = parentId,
	name = name,
	classname = classname,
	type = type
)