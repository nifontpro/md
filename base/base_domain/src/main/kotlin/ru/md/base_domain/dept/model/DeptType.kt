package ru.md.base_domain.dept.model

enum class DeptType(val code: String) {
	ROOT("R"),
	USER_OWNER("U"),
	SIMPLE("D"),
	UNDEF("N")
}