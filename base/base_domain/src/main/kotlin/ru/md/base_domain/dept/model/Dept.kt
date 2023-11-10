package ru.md.base_domain.dept.model

data class Dept(
	val id: Long = 0,
	val parentId: Long = 0,
	val name: String = "",
	val classname: String? = null,
	val topLevel: Boolean = false,
	val level: Int = 0,
	var mainImg: String? = null,
	val normImg: String? = null,
	val type: DeptType = DeptType.UNDEF,
	val companyName: String? = null,
)