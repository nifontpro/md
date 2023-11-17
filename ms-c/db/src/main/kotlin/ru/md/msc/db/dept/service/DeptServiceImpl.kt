package ru.md.msc.db.dept.service

import jakarta.transaction.Transactional
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import ru.md.base_db.base.mapper.toSort
import ru.md.base_db.dept.model.DeptImageEntity
import ru.md.base_db.dept.model.mappers.toDept
import ru.md.base_db.dept.repo.BaseDeptRepository
import ru.md.base_db.image.mappers.toBaseImage
import ru.md.base_domain.dept.biz.errors.DeptNotFoundException
import ru.md.base_domain.dept.biz.errors.TopLevelDeptNotFoundException
import ru.md.base_domain.dept.model.Dept
import ru.md.base_domain.errors.ImageNotFoundException
import ru.md.base_domain.image.model.BaseImage
import ru.md.base_domain.model.BaseOrder
import ru.md.msc.db.dept.model.mappers.toDeptDetails
import ru.md.msc.db.dept.model.mappers.toDeptDetailsEntity
import ru.md.msc.db.dept.repo.DeptDetailsRepository
import ru.md.msc.db.dept.repo.DeptImageRepository
import ru.md.msc.db.dept.repo.DeptRepository
import ru.md.msc.domain.dept.model.DeptDetails
import ru.md.msc.domain.dept.service.DeptService
import java.time.LocalDateTime

@Service
class DeptServiceImpl(
	private val deptRepository: DeptRepository,
	private val baseDeptRepository: BaseDeptRepository,
	private val deptDetailsRepository: DeptDetailsRepository,
	private val deptImageRepository: DeptImageRepository,
) : DeptService {

	@Transactional
	override fun create(deptDetails: DeptDetails): DeptDetails {
		val deptDetailsEntity = deptDetails.toDeptDetailsEntity(create = true)
		deptDetailsRepository.save(deptDetailsEntity)
		return deptDetailsEntity.toDeptDetails()
	}

	@Transactional
	override fun update(deptDetails: DeptDetails): DeptDetails {
		val oldDeptDetailsEntity =
			deptDetailsRepository.findByIdOrNull(deptDetails.dept.id) ?: throw DeptNotFoundException()
		with(oldDeptDetailsEntity) {
			dept.name = deptDetails.dept.name
			dept.classname = deptDetails.dept.classname
			dept.topLevel = deptDetails.dept.topLevel
			address = deptDetails.address
			email = deptDetails.email
			phone = deptDetails.phone
			description = deptDetails.description
		}
		return oldDeptDetailsEntity.toDeptDetails()
	}


	override fun findSubTreeDepts(deptId: Long, orders: List<BaseOrder>): List<Dept> {
		val ids = baseDeptRepository.subTreeIds(deptId = deptId)
		val depts = deptRepository.findByIdIn(ids = ids, sort = orders.toSort())
		return depts.map { it.toDept() }
	}


	override fun getTopLevelTreeDepts(deptId: Long, orders: List<BaseOrder>): List<Dept> {
		val topLevelId = baseDeptRepository.getTopLevelId(deptId = deptId) ?: throw TopLevelDeptNotFoundException()
		val ids = baseDeptRepository.subTreeIds(deptId = topLevelId)
		val depts = deptRepository.findByIdIn(ids = ids, sort = orders.toSort())
		return depts.map { it.toDept() }
	}

	/**
	 * Получение отделов по parentId
	 */
	override fun getDeptsByParentId(parentId: Long, orders: List<BaseOrder>): List<Dept> {
		val depts = deptRepository.findByParentId(parentId = parentId, sort = orders.toSort())
		return depts.map { it.toDept() }
	}

	@Transactional
	override fun findByIdDetails(deptId: Long): DeptDetails? {
		return deptDetailsRepository.findByIdOrNull(deptId)?.toDeptDetails()
	}

	override fun findById(deptId: Long): Dept? {
		return deptRepository.findByIdOrNull(deptId)?.toDept()
	}

	@Transactional
	override fun deleteById(deptId: Long) {
		deptRepository.deleteById(deptId)
	}


	@Transactional
	override fun addImage(deptId: Long, baseImage: BaseImage): BaseImage {
		val deptImageEntity = DeptImageEntity(
			deptId = deptId,
			originUrl = baseImage.originUrl,
			originKey = baseImage.originKey,
			normalUrl = baseImage.normalUrl,
			normalKey = baseImage.normalKey,
			miniUrl = baseImage.miniUrl,
			miniKey = baseImage.miniKey,
			type = baseImage.type,
			createdAt = LocalDateTime.now()
		)
		deptImageRepository.save(deptImageEntity)
		return deptImageEntity.toBaseImage()
	}

	@Transactional
	override fun deleteImage(deptId: Long, imageId: Long): BaseImage {
		val deptImageEntity = deptImageRepository.findByIdAndDeptId(deptId = deptId, imageId = imageId) ?: run {
			throw ImageNotFoundException()
		}
		deptImageRepository.delete(deptImageEntity)
		return deptImageEntity.toBaseImage()
	}

	@Transactional
	override fun setMainImage(deptId: Long): BaseImage? {
		val deptDetailsEntity = deptDetailsRepository.findByIdOrNull(deptId) ?: throw DeptNotFoundException()
		val deptEntity = deptDetailsEntity.dept

		var deptImageEntity = deptDetailsEntity.images.firstOrNull() ?: run {
			deptEntity.mainImg = null
			deptEntity.normImg = null
			return null
		}

		deptDetailsEntity.images.forEach {
			if (it.createdAt > deptImageEntity.createdAt) {
				deptImageEntity = it
			} else if (it.main) {
				it.main = false
			}
		}

		deptImageEntity.main = true
		deptEntity.mainImg =
			if (deptImageEntity.miniUrl != null) deptImageEntity.miniUrl else deptImageEntity.normalUrl
		deptEntity.normImg = deptImageEntity.normalUrl
		return deptImageEntity.toBaseImage()
	}

	override fun checkDeptExist(parentId: Long, name: String): Boolean {
		return deptRepository.countByParentIdAndNameIgnoreCase(parentId, name) > 0
	}

	// Service
	@Transactional
	override fun updateAllDeptImg() {
		val depts = deptRepository.findAll()
		depts.forEach {
			val id = it.id ?: return@forEach
			val img = setMainImage(id)
			println(img)
		}
	}

	override fun findByIdsAndName(ids: List<Long>, name: String): List<Dept> {
		return deptRepository.findByIdInAndNameIgnoreCase(
			deptsIds = ids,
			name = name
		).map { it.toDept() }
	}

}