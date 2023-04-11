package ru.md.msc.domain.dept.model

enum class DeptType(val code: String) {
	ROOT("R"),
	USER("U"),
	SIMPLE("D"),
	UNDEF("N")
}