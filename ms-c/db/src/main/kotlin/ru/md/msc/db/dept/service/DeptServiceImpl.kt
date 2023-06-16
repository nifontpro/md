package ru.md.msc.db.dept.service

import jakarta.transaction.Transactional
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import ru.md.base_db.mapper.toImage
import ru.md.base_db.mapper.toSort
import ru.md.base_domain.image.model.BaseImage
import ru.md.base_domain.image.model.ImageType
import ru.md.base_domain.model.BaseOrder
import ru.md.msc.db.dept.model.image.DeptImageEntity
import ru.md.msc.db.dept.model.mappers.toDeptDetails
import ru.md.msc.db.dept.model.mappers.toDeptDetailsEntity
import ru.md.msc.db.dept.model.mappers.toDeptLazy
import ru.md.msc.db.dept.repo.DeptDetailsRepository
import ru.md.msc.db.dept.repo.DeptImageRepository
import ru.md.msc.db.dept.repo.DeptRepository
import ru.md.msc.domain.base.biz.ImageNotFoundException
import ru.md.msc.domain.dept.biz.proc.DeptNotFoundException
import ru.md.msc.domain.dept.biz.proc.TopLevelDeptNotFoundException
import ru.md.msc.domain.dept.model.Dept
import ru.md.msc.domain.dept.model.DeptDetails
import ru.md.msc.domain.dept.service.DeptService
import java.time.LocalDateTime

@Service
@Transactional
class DeptServiceImpl(
	private val deptRepository: DeptRepository,
	private val deptDetailsRepository: DeptDetailsRepository,
	private val deptImageRepository: DeptImageRepository,
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
				it.topLevel = deptDetails.dept.topLevel
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

	/**
	 * Получить ids всех элементов поддерева отделов, включая вершину
	 */
	override fun findSubTreeIds(deptId: Long): List<Long> {
		return deptRepository.subTreeIds(deptId = deptId)
	}

	override fun findSubTreeDepts(deptId: Long, orders: List<BaseOrder>): List<Dept> {
		val ids = deptRepository.subTreeIds(deptId = deptId)
		val depts = deptRepository.findByIdIn(ids = ids, sort = orders.toSort())
		return depts.map { it.toDeptLazy() }
	}

	override fun getTopLevelTreeDepts(deptId: Long, orders: List<BaseOrder>): List<Dept> {
		val topLevelId = deptRepository.getTopLevelId(deptId = deptId) ?: throw TopLevelDeptNotFoundException()
		val ids = deptRepository.subTreeIds(deptId = topLevelId)
		val depts = deptRepository.findByIdIn(ids = ids, sort = orders.toSort())
		return depts.map { it.toDeptLazy() }
	}

	override fun findByIdDetails(deptId: Long): DeptDetails? {
		return deptDetailsRepository.findByIdOrNull(deptId)?.toDeptDetails()
	}

	override fun deleteById(deptId: Long) {
		deptRepository.deleteById(deptId)
	}

	/**
	 * Получение id отдела корневого Владельца
	 */
	override fun getRootId(deptId: Long): Long? {
		return deptRepository.getRootId(deptId = deptId)
	}

	override fun addImage(deptId: Long, baseImage: BaseImage): BaseImage {
		val deptImageEntity = DeptImageEntity(
			deptId = deptId,
			imageUrl = baseImage.imageUrl,
			imageKey = baseImage.imageKey,
			type = ImageType.USER,
			createdAt = LocalDateTime.now()
		)
		deptImageRepository.save(deptImageEntity)
		return deptImageEntity.toImage()
	}

	override fun deleteImage(deptId: Long, imageId: Long): BaseImage {
		val deptImageEntity = deptImageRepository.findByIdAndDeptId(deptId = deptId, imageId = imageId) ?: run {
			throw ImageNotFoundException()
		}
		deptImageRepository.delete(deptImageEntity)
		return deptImageEntity.toImage()
	}

	override fun setMainImage(deptId: Long): BaseImage? {
		val deptEntity = deptRepository.findByIdOrNull(deptId) ?: throw DeptNotFoundException()
		val images = deptEntity.images
		var deptImageEntity = images.firstOrNull() ?: run {
			deptEntity.mainImg = null
			return null
		}

		images.forEach {
			if (it.createdAt > deptImageEntity.createdAt) {
				deptImageEntity = it
			}
		}
		deptEntity.mainImg = deptImageEntity.imageUrl
		return deptImageEntity.toImage()
	}

	override fun updateAllDeptImg() {
		val depts = deptRepository.findAll()
		depts.forEach {
			val id = it.id ?: return@forEach
			val img = setMainImage(id)
			println(img)
		}
	}

}