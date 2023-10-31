package ru.md.base_domain.dept.service

import ru.md.base_domain.dept.model.Dept

interface BaseDeptService {
	fun validateDeptChild(upId: Long, downId: Long): Boolean
	fun validateUserLevel(upId: Long, userId: Long): Boolean
	fun findSubTreeIds(deptId: Long): List<Long>
	fun findTopLevelDeptId(deptId: Long): Long
	fun findTopLevelDept(deptId: Long): Dept
	fun getRootId(deptId: Long): Long?
	fun getCompanyDeptId(deptId: Long): Long
}