package ru.md.msc.domain.dept.service

import ru.md.base_domain.image.model.BaseImage
import ru.md.base_domain.model.BaseOrder
import ru.md.base_domain.dept.model.Dept
import ru.md.msc.domain.dept.model.DeptDetails

interface DeptService {
	fun create(deptDetails: DeptDetails): DeptDetails
	fun findSubTreeDepts(deptId: Long, orders: List<BaseOrder> = emptyList()): List<Dept>
	fun findByIdDetails(deptId: Long): DeptDetails?
	fun deleteById(deptId: Long)
	fun update(deptDetails: DeptDetails): DeptDetails
	fun addImage(deptId: Long, baseImage: BaseImage): BaseImage
	fun deleteImage(deptId: Long, imageId: Long): BaseImage
	fun updateAllDeptImg()
	fun setMainImage(deptId: Long): BaseImage?
	fun getTopLevelTreeDepts(deptId: Long, orders: List<BaseOrder>): List<Dept>
	fun getDeptsByParentId(parentId: Long, orders: List<BaseOrder>): List<Dept>
	fun findById(deptId: Long): Dept?
	fun checkDeptExist(parentId: Long, name: String): Boolean
}