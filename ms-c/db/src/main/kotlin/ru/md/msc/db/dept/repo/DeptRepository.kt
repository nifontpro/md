package ru.md.msc.db.dept.repo

import org.springframework.data.domain.Sort
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import ru.md.base_db.dept.model.DeptEntity

@Repository
interface DeptRepository : JpaRepository<DeptEntity, Long> {

	fun findByIdIn(ids: List<Long>, sort: Sort): List<DeptEntity>

	@Modifying
	@Query("delete from DeptEntity d where d.id = :deptId")
	override fun deleteById(deptId: Long)

	fun findByParentId(parentId: Long, sort: Sort): List<DeptEntity>

	fun countByParentIdAndNameIgnoreCase(parentId: Long, name: String): Int
}