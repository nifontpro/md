package ru.md.msc.db.dept.model.mappers

import ru.md.msc.db.dept.model.DeptEntity
import ru.md.msc.domain.dept.model.Dept

fun DeptEntity.toDept() = Dept(
	id = id,
	parentId = parentId,
	name = name,
	classname = classname,
	type = type,
)