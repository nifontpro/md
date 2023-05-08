package ru.md.msc.domain.dept.service

import ru.md.msc.domain.base.model.BaseOrder
import ru.md.msc.domain.dept.model.Dept
import ru.md.msc.domain.dept.model.DeptDetails
import ru.md.msc.domain.image.model.BaseImage

interface DeptService {
	fun create(deptDetails: DeptDetails): DeptDetails
	fun validateDeptLevel(upId: Long, downId: Long): Boolean
	fun findSubTreeDepts(deptId: Long, orders: List<BaseOrder> = emptyList()): List<Dept>
	fun validateUserLevel(upId: Long, userId: Long): Boolean
	fun findByIdDetails(deptId: Long): DeptDetails?
	fun deleteById(deptId: Long)
	fun update(deptDetails: DeptDetails): DeptDetails
	fun addImage(deptId: Long, baseImage: BaseImage): BaseImage
	fun deleteImage(deptId: Long, imageId: Long): BaseImage
	fun getRootId(deptId: Long): Long?
	fun findSubTreeIds(deptId: Long): List<Long>
}