package ru.md.base_db.dept.repo

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import ru.md.base_db.dept.model.DeptEntity

@Repository
interface BaseDeptRepository : JpaRepository<DeptEntity, Long> {

	/**
	 * Хранимая процедура проверяет, является ли отдел [upId]
	 * предком отдела [downId] (нижнего в иерархии отделов)
	 */
//	@Procedure(procedureName = "dep.up_tree_has_id") // Не работает с версии Spring Boot 3.1.x
	@Query("select * from dep.up_tree_has_id(:downId, :upId)", nativeQuery = true)
	fun upTreeHasDeptId(downId: Long, upId: Long): Boolean

	/**
	 * Хранимая процедура проверяет, является ли сотрудник [userId]
	 * потомком отдела [upId]
	 */
	@Query("select * from dep.check_user_child(:userId, :upId)", nativeQuery = true)
	fun checkUserChild(userId: Long, upId: Long): Boolean

	/**
	 * Получить ids всех элементов поддерева отделов, включая вершину
	 */
	@Query("select * from dep.sub_tree_ids(:deptId)", nativeQuery = true)
	fun subTreeIds(deptId: Long): List<Long>

	/**
	 * Получить корневой отдел (тот который создал владелец) от текущего [deptId]
	 */
	@Query("select * from dep.get_root_id(:deptId)", nativeQuery = true)
	fun getOwnerRootId(deptId: Long): Long?

	/**
	 * Получить отдел компании (level = 2) от текущего [deptId]
	 */
	@Query("select * from dep.get_company_level_id(:deptId)", nativeQuery = true)
	fun getCompanyDeptId(deptId: Long): Long?

	/**
	 * Получить отдел верхнего уровня просмотра (top-level) от текущего [deptId]
	 */
	@Query("select * from dep.get_top_level_id(:deptId)", nativeQuery = true)
	fun getTopLevelId(deptId: Long): Long?

	/**
	 * Получить ids непосредственных (ближних) потомков отдела
	 */
	@Query("select d.id from DeptEntity d where d.parentId=:parentId")
	fun findChildIdsByParentId(parentId: Long): List<Long>


}