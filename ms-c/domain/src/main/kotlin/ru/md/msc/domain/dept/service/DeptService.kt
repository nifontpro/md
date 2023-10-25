package ru.md.msc.domain.dept.service

import ru.md.base_domain.image.model.BaseImage
import ru.md.base_domain.model.BaseOrder
import ru.md.base_domain.dept.model.Dept
import ru.md.msc.domain.dept.model.DeptDetails

interface DeptService {
	fun create(deptDetails: DeptDetails): DeptDetails
	fun validateDeptChild(upId: Long, downId: Long): Boolean
	fun findSubTreeDepts(deptId: Long, orders: List<BaseOrder> = emptyList()): List<Dept>
	fun validateUserLevel(upId: Long, userId: Long): Boolean
	fun findByIdDetails(deptId: Long): DeptDetails?
	fun deleteById(deptId: Long)
	fun update(deptDetails: DeptDetails): DeptDetails
	fun addImage(deptId: Long, baseImage: BaseImage): BaseImage
	fun deleteImage(deptId: Long, imageId: Long): BaseImage
	fun getRootId(deptId: Long): Long?
	fun findSubTreeIds(deptId: Long): List<Long>
	fun updateAllDeptImg()
	fun setMainImage(deptId: Long): BaseImage?
	fun getTopLevelTreeDepts(deptId: Long, orders: List<BaseOrder>): List<Dept>
	fun findTopLevelDeptId(deptId: Long): Long
	fun getDeptsByParentId(parentId: Long, orders: List<BaseOrder>): List<Dept>
	fun findById(deptId: Long): Dept?
	fun findTopLevelDept(deptId: Long): Dept
	fun checkDeptExist(parentId: Long, name: String): Boolean
}