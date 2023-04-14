package ru.md.msc.domain.dept.service

import ru.md.msc.domain.base.model.RepositoryData
import ru.md.msc.domain.dept.model.Dept
import ru.md.msc.domain.dept.model.DeptDetails

interface DeptService {
	fun create(deptDetails: DeptDetails): RepositoryData<DeptDetails>
	fun validateDeptLevel(upId: Long, downId: Long): RepositoryData<Boolean>
	fun findSubTreeDepts(deptId: Long): RepositoryData<List<Dept>>
}