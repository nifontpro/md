package ru.md.msc.db.dept.service

import org.springframework.stereotype.Service
import ru.md.base_db.dept.repo.BaseDeptRepository

@Service
class DeptUtil(
	private val baseDeptRepository: BaseDeptRepository,
) {
	/**
	 * Получение списка отделов от текущей вершины дерева
	 * subdepts = true - все дерево подотделов
	 * subdepts = false:
	 *    nearSub = false (default) - только вершина
	 *            = true - непосредственные потомки
	 */
	fun getDepts(deptId: Long, subdepts: Boolean, nearSub: Boolean = false): List<Long> {
		return if (subdepts) {
			baseDeptRepository.subTreeIds(deptId = deptId)
		} else {
			if (nearSub) {
				baseDeptRepository.findChildIdsByParentId(parentId = deptId)
			} else {
				listOf(deptId)
			}
		}
	}

	/**
	 * Получить ids всех отделов иерархии от Владельца
	 * deptId может быть в любом месте дерева
	 */
	fun getAllDeptIds(deptId: Long): List<Long> {
		val rootId = baseDeptRepository.getOwnerRootId(deptId = deptId) ?: return emptyList()
		return getDepts(deptId = rootId, subdepts = true)
	}
}