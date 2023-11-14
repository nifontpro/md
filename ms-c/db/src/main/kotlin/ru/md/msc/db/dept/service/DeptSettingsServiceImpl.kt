package ru.md.msc.db.dept.service

import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import ru.md.msc.db.dept.model.mappers.toDeptSettings
import ru.md.msc.db.dept.model.mappers.toDeptSettingsEntity
import ru.md.msc.db.dept.repo.DeptSettingsRepository
import ru.md.msc.domain.dept.model.DeptSettings
import ru.md.msc.domain.dept.service.DeptSettingsService

@Service
class DeptSettingsServiceImpl(
	private val deptSettingsRepository: DeptSettingsRepository
) : DeptSettingsService {

	@Transactional
	override fun saveSettings(deptSettings: DeptSettings): DeptSettings {
		val deptSettingsEntity = deptSettingsRepository.findByDeptId(deptSettings.deptId) ?: run {
			val newDeptSettingsEntity = deptSettings.toDeptSettingsEntity()
			deptSettingsRepository.save(newDeptSettingsEntity)
			return newDeptSettingsEntity.toDeptSettings()
		}
		deptSettingsEntity.payName = deptSettings.payName
		return deptSettingsEntity.toDeptSettings()
	}

	override fun getSettings(deptId: Long): DeptSettings? {
		return deptSettingsRepository.findByDeptId(deptId)?.toDeptSettings()
	}

}