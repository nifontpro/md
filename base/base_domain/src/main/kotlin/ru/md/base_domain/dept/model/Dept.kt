package ru.md.base_domain.dept.model

import ru.md.base_domain.image.model.BaseImage

data class Dept(
	val id: Long = 0,
	val parentId: Long = 0,
	val name: String = "",
	val classname: String? = null,
	val topLevel: Boolean = false,
	val level: Int = 0,
	var mainImg: String? = null,
	val type: DeptType = DeptType.UNDEF,
	val images: List<BaseImage> = emptyList(),
)