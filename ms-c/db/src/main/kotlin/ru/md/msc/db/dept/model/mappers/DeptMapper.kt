package ru.md.msc.db.dept.model.mappers

import ru.md.base_db.mapper.toBaseImage
import ru.md.msc.db.dept.model.DeptEntity
import ru.md.msc.domain.dept.model.Dept

fun DeptEntity.toDept() = Dept(
	id = id ?: 0,
	parentId = parentId ?: 0,
	name = name,
	classname = classname,
	topLevel = topLevel,
	level = level,
	type = type,
	mainImg = mainImg,
	images = images.map { it.toBaseImage() }
)

fun DeptEntity.toDeptLazy() = Dept(
	id = id ?: 0,
	parentId = parentId ?: 0,
	name = name,
	classname = classname,
	topLevel = topLevel,
	level = level,
	type = type,
	mainImg = mainImg,
)

fun Dept.toDeptEntity(create: Boolean = false) = DeptEntity(
	id = if (create) null else id,
	parentId = parentId,
	name = name,
	classname = classname,
	topLevel = topLevel,
	type = type
)