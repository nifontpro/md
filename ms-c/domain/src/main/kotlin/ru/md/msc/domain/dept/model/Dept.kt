package ru.md.msc.domain.dept.model

data class Dept(
	val id: Long? = null,
	val parentId: Long? = null,
	val name: String? = null,
	val classname: String? = null,
	val type: DeptType = DeptType.UNDEF,
)