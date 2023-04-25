package ru.md.msc.db.dept.service

import jakarta.transaction.Transactional
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import ru.md.msc.db.base.mapper.toImage
import ru.md.msc.db.dept.model.image.DeptImageEntity
import ru.md.msc.db.dept.model.mappers.toDept
import ru.md.msc.db.dept.model.mappers.toDeptDetails
import ru.md.msc.db.dept.model.mappers.toDeptDetailsEntity
import ru.md.msc.db.dept.repo.DeptDetailsRepository
import ru.md.msc.db.dept.repo.DeptImageRepository
import ru.md.msc.db.dept.repo.DeptRepository
import ru.md.msc.domain.dept.biz.proc.DeptNotFoundException
import ru.md.msc.domain.dept.model.Dept
import ru.md.msc.domain.dept.model.DeptDetails
import ru.md.msc.domain.dept.service.DeptService
import ru.md.msc.domain.image.model.BaseImage
import ru.md.msc.domain.image.model.FileData
import ru.md.msc.domain.image.model.ImageType
import ru.md.msc.domain.image.repository.S3Repository
import java.time.LocalDateTime

@Service
@Transactional
class DeptServiceImpl(
	private val deptRepository: DeptRepository,
	private val deptDetailsRepository: DeptDetailsRepository,
	private val deptImageRepository: DeptImageRepository,
	private val s3Repository: S3Repository,
) : DeptService {

	override fun create(deptDetails: DeptDetails): DeptDetails {
		val deptDetailsEntity = deptDetails.toDeptDetailsEntity(create = true)
		deptDetailsRepository.save(deptDetailsEntity)
		return deptDetailsEntity.toDeptDetails()
	}

	override fun update(deptDetails: DeptDetails): DeptDetails {
		val oldDeptDetailsEntity =
			deptDetailsRepository.findByIdOrNull(deptDetails.dept.id) ?: throw DeptNotFoundException()
		with(oldDeptDetailsEntity) {
			dept?.let {
				it.name = deptDetails.dept.name
				it.classname = deptDetails.dept.classname
			}
			address = deptDetails.address
			email = deptDetails.email
			phone = deptDetails.phone
			description = deptDetails.description
		}
		return oldDeptDetailsEntity.toDeptDetails()
	}

	/**
	 * Проверка, является ли отдел [downId] потомком [upId] в дереве отделов
	 */
	override fun validateDeptLevel(upId: Long, downId: Long): Boolean {
		return deptRepository.upTreeHasDeptId(downId = downId, upId = upId)
	}

	/**
	 * Проверка, является ли сотрудник [userId] потомком отдела [upId] в дереве отделов
	 */
	override fun validateUserLevel(upId: Long, userId: Long): Boolean {
		return deptRepository.checkUserChild(userId = userId, upId = upId)
	}

	override fun findSubTreeDepts(deptId: Long): List<Dept> {
		val ids = deptRepository.subTreeIds(deptId = deptId)
		val depts = deptRepository.findByIdIn(ids = ids)
		return depts.map { it.toDept() }
	}

	override fun findByIdDetails(deptId: Long): DeptDetails? {
		return deptDetailsRepository.findByIdOrNull(deptId)?.toDeptDetails()
	}

	override fun deleteById(deptId: Long) {
		deptRepository.deleteById(deptId)
	}

	override suspend fun addImage(deptId: Long, fileData: FileData): BaseImage {
		val rootDeptId = deptRepository.getRootId(deptId = deptId) ?: throw Exception()
		val prefix = "R$rootDeptId/D$deptId/IMAGES"
		val imageKey = "$prefix/${fileData.filename}"
		val imageUrl = s3Repository.putObject(key = imageKey, fileData = fileData) ?: throw Exception()

		val deptImageEntity = DeptImageEntity(
			deptId = deptId,
			imageUrl = imageUrl,
			imageKey = imageKey,
			type = ImageType.USER,
			createdAt = LocalDateTime.now()
		)
		// если id=null or Exception, то удалить изображение
		deptImageRepository.save(deptImageEntity)
		return deptImageEntity.toImage()
	}

}