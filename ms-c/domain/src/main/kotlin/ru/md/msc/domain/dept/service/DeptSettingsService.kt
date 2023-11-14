package ru.md.msc.domain.dept.service

import ru.md.msc.domain.dept.model.DeptSettings

interface DeptSettingsService {
	fun saveSettings(deptSettings: DeptSettings): DeptSettings
	fun getSettings(deptId: Long): DeptSettings?
}