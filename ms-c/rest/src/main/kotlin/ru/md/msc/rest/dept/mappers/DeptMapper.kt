package ru.md.msc.rest.dept.mappers

import ru.md.base_domain.dept.model.Dept
import ru.md.base_rest.model.response.BaseImageResponse
import ru.md.msc.rest.dept.model.response.DeptResponse

fun Dept.toDeptResponse(images: List<BaseImageResponse> = emptyList()) = DeptResponse(
	id = id,
	parentId = parentId,
	name = name,
	classname = classname,
	topLevel = topLevel,
	level = level,
	mainImg = mainImg,
	normImg = normImg,
	type = type,
	images = images
)