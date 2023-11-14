package ru.md.msc.db.dept.model.mappers

import ru.md.msc.db.dept.model.DeptSettingsEntity
import ru.md.msc.domain.dept.model.DeptSettings

fun DeptSettings.toDeptSettingsEntity() = DeptSettingsEntity(
//	id = if (id == 0L) null else id,
	deptId = deptId,
	payName = payName
)

fun DeptSettingsEntity.toDeptSettings() = DeptSettings(
	id = id ?: 0,
	deptId = deptId,
	payName = payName
)