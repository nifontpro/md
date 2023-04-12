package ru.md.msc.db.dept.repo

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.query.Procedure
import org.springframework.stereotype.Repository
import ru.md.msc.db.dept.model.DeptEntity

@Repository
interface DeptRepository : JpaRepository<DeptEntity, Long> {

	/**
	 * Хранимая процедура проверяет, является ли отдел upId
	 * предком отдела downId (нижнего в иерархии отделов)
	 */
	@Procedure(procedureName = "dep.up_tree_has_id")
	fun upTreeHasDeptId(downId: Long, upId: Long): Boolean
}