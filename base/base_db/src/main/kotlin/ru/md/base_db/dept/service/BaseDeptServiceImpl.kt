package ru.md.base_db.dept.service

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import ru.md.base_db.dept.model.mappers.toDept
import ru.md.base_domain.dept.model.Dept
import ru.md.base_db.dept.repo.BaseDeptRepository
import ru.md.base_domain.dept.biz.errors.TopLevelDeptNotFoundException
import ru.md.base_domain.dept.service.BaseDeptService

@Service
class BaseDeptServiceImpl(
	private val baseDeptRepository: BaseDeptRepository,
): BaseDeptService {

	/**
	 * Проверка, является ли отдел [downId] потомком [upId] в дереве отделов
	 */
	override fun validateDeptChild(upId: Long, downId: Long): Boolean {
		return baseDeptRepository.upTreeHasDeptId(downId = downId, upId = upId)
	}

	/**
	 * Проверка, является ли сотрудник [userId] потомком отдела [upId] в дереве отделов
	 */
	override fun validateUserLevel(upId: Long, userId: Long): Boolean {
		return baseDeptRepository.checkUserChild(userId = userId, upId = upId)
	}

	/**
	 * Получить ids всех элементов поддерева отделов, включая вершину
	 */
	override fun findSubTreeIds(deptId: Long): List<Long> {
		return baseDeptRepository.subTreeIds(deptId = deptId)
	}

	override fun findTopLevelDeptId(deptId: Long): Long {
		return baseDeptRepository.getTopLevelId(deptId = deptId) ?: throw TopLevelDeptNotFoundException()
	}

	override fun findTopLevelDept(deptId: Long): Dept {
		val topDeptId = baseDeptRepository.getTopLevelId(deptId = deptId) ?: throw TopLevelDeptNotFoundException()
		return baseDeptRepository.findByIdOrNull(topDeptId)?.toDept() ?: throw TopLevelDeptNotFoundException()
	}

	/**
	 * Получение id отдела корневого Владельца
	 */
	override fun getRootId(deptId: Long): Long? {
		return baseDeptRepository.getOwnerRootId(deptId = deptId)
	}

}