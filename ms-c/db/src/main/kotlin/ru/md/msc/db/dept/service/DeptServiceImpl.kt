package ru.md.msc.db.dept.service

import jakarta.transaction.Transactional
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import ru.md.msc.db.dept.model.mappers.toDept
import ru.md.msc.db.dept.model.mappers.toDeptDetails
import ru.md.msc.db.dept.model.mappers.toDeptDetailsEntity
import ru.md.msc.db.dept.repo.DeptDetailsRepository
import ru.md.msc.db.dept.repo.DeptRepository
import ru.md.msc.domain.dept.model.Dept
import ru.md.msc.domain.dept.model.DeptDetails
import ru.md.msc.domain.dept.service.DeptService

@Service
@Transactional
class DeptServiceImpl(
	private val deptRepository: DeptRepository,
	private val deptDetailsRepository: DeptDetailsRepository
) : DeptService {

	override fun create(deptDetails: DeptDetails): DeptDetails {
		val deptDetailsEntity = deptDetails.toDeptDetailsEntity(create = true)
		deptDetailsRepository.save(deptDetailsEntity)
		return deptDetailsEntity.toDeptDetails()
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

}