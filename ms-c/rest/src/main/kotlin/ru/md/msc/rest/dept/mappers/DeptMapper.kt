package ru.md.msc.rest.dept.mappers

import ru.md.base_rest.model.mapper.toBaseImageResponse
import ru.md.base_domain.dept.model.Dept
import ru.md.msc.rest.dept.model.response.DeptResponse

fun Dept.toDeptResponse() = DeptResponse(
	id = id,
	parentId = parentId,
	name = name,
	classname = classname,
	topLevel = topLevel,
	level = level,
	mainImg = mainImg,
	normImg = normImg,
	type = type,
	images = images.map { it.toBaseImageResponse() }
)