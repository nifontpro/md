package ru.md.msc.domain.dept.model

data class Dept(
	val id: Long? = null,
	val parentId: Long = 0,
	val name: String = "",
	val classname: String? = null,
	val type: DeptType = DeptType.UNDEF,
)