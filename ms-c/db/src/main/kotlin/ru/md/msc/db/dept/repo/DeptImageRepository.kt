package ru.md.msc.db.dept.repo

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import ru.md.base_db.dept.model.DeptImageEntity

@Repository
interface DeptImageRepository : JpaRepository<DeptImageEntity, Long> {

	@Query("from DeptImageEntity i where i.id = :imageId and i.deptId = :deptId")
	fun findByIdAndDeptId(imageId: Long, deptId: Long): DeptImageEntity?

}