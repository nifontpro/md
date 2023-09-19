package ru.md.msc.db.dept.service

import org.springframework.stereotype.Service
import ru.md.msc.db.dept.repo.DeptRepository

@Service
class DeptUtil(
	private val deptRepository: DeptRepository,
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
			deptRepository.subTreeIds(deptId = deptId)
		} else {
			if (nearSub) {
				deptRepository.findChildIdsByParentId(parentId = deptId)
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
		val rootId = deptRepository.getOwnerRootId(deptId = deptId) ?: return emptyList()
		return getDepts(deptId = rootId, subdepts = true)
	}
}