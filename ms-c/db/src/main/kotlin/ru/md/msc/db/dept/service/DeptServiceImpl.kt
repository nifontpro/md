package ru.md.msc.db.dept.service

import jakarta.transaction.Transactional
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import ru.md.msc.db.dept.model.mappers.toDept
import ru.md.msc.db.dept.model.mappers.toDeptDetails
import ru.md.msc.db.dept.model.mappers.toDeptDetailsEntity
import ru.md.msc.db.dept.repo.DeptDetailsRepository
import ru.md.msc.db.dept.repo.DeptRepository
import ru.md.msc.domain.base.model.RepositoryData
import ru.md.msc.domain.dept.model.Dept
import ru.md.msc.domain.dept.model.DeptDetails
import ru.md.msc.domain.dept.service.DeptService

@Service
@Transactional
class DeptServiceImpl(
	private val deptRepository: DeptRepository,
	private val deptDetailsRepository: DeptDetailsRepository
) : DeptService {

	override fun create(deptDetails: DeptDetails): RepositoryData<DeptDetails> {
		val deptDetailsEntity = deptDetails.toDeptDetailsEntity(create = true)
		try {
			deptDetailsRepository.save(deptDetailsEntity)
		} catch (e: Exception) {
			log.error(e.message)
			return DeptErrors.createDept()
		}
		return RepositoryData.success(deptDetailsEntity.toDeptDetails())
	}

	/**
	 * Проверка, является ли отдел [downId] потомком [upId] в дереве отделов
	 */
	override fun validateDeptLevel(upId: Long, downId: Long): RepositoryData<Boolean> {
		return try {
			val res = deptRepository.upTreeHasDeptId(downId = downId, upId = upId)
			RepositoryData.success(data = res)
		} catch (e: Exception) {
			DeptErrors.getDeptAuth()
		}
	}

	/**
	 * Проверка, является ли сотрудник [userId] потомком отдела [upId] в дереве отделов
	 */
	override fun validateUserLevel(upId: Long, userId: Long): RepositoryData<Boolean> {
		return try {
			val res = deptRepository.checkUserChild(userId = userId, upId = upId)
			RepositoryData.success(data = res)
		} catch (e: Exception) {
			DeptErrors.getDeptAuth()
		}
	}

	override fun findSubTreeDepts(deptId: Long): RepositoryData<List<Dept>> {
		val ids = deptRepository.subTreeIds(deptId = deptId)
		return try {
			val depts = deptRepository.findByIdIn(ids = ids)
			RepositoryData.success(data = depts.map { it.toDept() })
		} catch (e: Exception) {
			DeptErrors.getDepts()
		}
	}

	companion object {
		var log: Logger = LoggerFactory.getLogger(DeptServiceImpl::class.java)
	}

}