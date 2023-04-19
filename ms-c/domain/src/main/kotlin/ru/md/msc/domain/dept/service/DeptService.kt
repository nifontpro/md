package ru.md.msc.domain.dept.service

import ru.md.msc.domain.dept.model.Dept
import ru.md.msc.domain.dept.model.DeptDetails

interface DeptService {
	fun create(deptDetails: DeptDetails): DeptDetails
	fun validateDeptLevel(upId: Long, downId: Long): Boolean
	fun findSubTreeDepts(deptId: Long): List<Dept>
	fun validateUserLevel(upId: Long, userId: Long): Boolean
	fun findByIdDetails(deptId: Long): DeptDetails?
	fun deleteById(deptId: Long)
}