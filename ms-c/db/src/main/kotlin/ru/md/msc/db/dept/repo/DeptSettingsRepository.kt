package ru.md.msc.db.dept.repo

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import ru.md.msc.db.dept.model.DeptSettingsEntity

@Repository
interface DeptSettingsRepository : JpaRepository<DeptSettingsEntity, Long> {

	fun findByDeptId(deptId: Long): DeptSettingsEntity?

}