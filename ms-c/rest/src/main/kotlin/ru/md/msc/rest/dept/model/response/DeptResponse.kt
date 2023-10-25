package ru.md.msc.rest.dept.model.response

import ru.md.base_rest.model.response.BaseImageResponse
import ru.md.base_domain.dept.model.DeptType

data class DeptResponse(
	val id: Long = 0,
	val parentId: Long = 0,
	val name: String = "",
	val classname: String? = null,
	val topLevel: Boolean = false,
	val level: Int = 0,
	var mainImg: String? = null,
	val type: DeptType = DeptType.UNDEF,
	val images: List<BaseImageResponse> = emptyList(),
)