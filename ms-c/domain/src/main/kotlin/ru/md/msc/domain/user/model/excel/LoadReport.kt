package ru.md.msc.domain.user.model.excel

data class LoadReport(
	val addReport: List<AddUserReport> = emptyList(),
	val createdDeptCount: Int = 0,
	val createdUserCount: Int = 0,
	val updatedUserCount: Int = 0,
)
