package ru.md.msc.db.dept.repo

import org.springframework.data.domain.Sort
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import ru.md.msc.db.dept.model.DeptEntity

@Repository
interface DeptRepository : JpaRepository<DeptEntity, Long> {

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
	 * Получить отдел верхнего уровня просмотра (top-level) от текущего [deptId]
	 */
	@Query("select * from dep.get_top_level_id(:deptId)", nativeQuery = true)
	fun getTopLevelId(deptId: Long): Long?

//	/**
//	 * Получить уровень отдела
//	 */
//	@Query("select * from dep.get_level(:deptId)", nativeQuery = true)
//	fun getLevel(deptId: Long): Long

	fun findByIdIn(ids: List<Long>, sort: Sort): List<DeptEntity>

	/**
	 * Получить ids непосредственных (ближних) потомков отдела
	 */
	@Query("select d.id from DeptEntity d where d.parentId=:parentId")
	fun findChildIdsByParentId(parentId: Long): List<Long>

	@Modifying
	@Query("delete from DeptEntity d where d.id = :deptId")
	override fun deleteById(deptId: Long)

	fun findByParentId(parentId: Long, sort: Sort): List<DeptEntity>
}