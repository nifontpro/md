package ru.md.msc.domain.dept.service

import ru.md.msc.domain.dept.model.Dept
import ru.md.msc.domain.dept.model.DeptDetails
import ru.md.msc.domain.image.model.BaseImage
import ru.md.msc.domain.image.model.FileData

interface DeptService {
	fun create(deptDetails: DeptDetails): DeptDetails
	fun validateDeptLevel(upId: Long, downId: Long): Boolean
	fun findSubTreeDepts(deptId: Long): List<Dept>
	fun validateUserLevel(upId: Long, userId: Long): Boolean
	fun findByIdDetails(deptId: Long): DeptDetails?
	fun deleteById(deptId: Long)
	fun update(deptDetails: DeptDetails): DeptDetails
    suspend fun addImage(deptId: Long, fileData: FileData): BaseImage
	suspend fun updateImage(deptId: Long, imageId: Long, fileData: FileData): BaseImage
	suspend fun deleteImage(deptId: Long, imageId: Long): BaseImage
}