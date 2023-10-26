package ru.md.msc.domain.dept.biz.workers.chain

import ru.md.base_domain.biz.proc.ContextState
import ru.md.base_domain.biz.validate.validateSortedFields
import ru.md.base_domain.biz.workers.finishOperation
import ru.md.cor.ICorChainDsl
import ru.md.cor.chain
import ru.md.cor.worker
import ru.md.base_domain.biz.validate.validateAuthDeptTopLevelForViewBool
import ru.md.base_domain.user.biz.workers.getAuthUserAndVerifyEmail
import ru.md.base_domain.dept.biz.validate.validateDeptId
import ru.md.msc.domain.dept.biz.proc.DeptContext
import ru.md.msc.domain.dept.biz.workers.getDeptsByParentId
import ru.md.msc.domain.dept.biz.workers.getTopLevelDeptByDeptId

/**
 * Цепочка получение списка отделов по parentId (deptId)
 */
fun ICorChainDsl<DeptContext>.getDeptListByParentDeptIdChain() {
	validateDeptId("Проверяем deptId")
	worker("Допустимые поля сортировки") { orderFields = listOf("name", "classname") }
	validateSortedFields("Проверка списка полей сортировки")
	getAuthUserAndVerifyEmail("Проверка авторизованного пользователя по authId")
	validateAuthDeptTopLevelForViewBool("Проверка доступа к чтению данных отдела")

	chain {
		on { isAuth && state == ContextState.RUNNING }
		getDeptsByParentId("Получаем потомков отдела deptId")
		finishOperation()
	}

	chain {
		on { !isAuth && state == ContextState.RUNNING }
		getTopLevelDeptByDeptId("Получаем верхний отдел авторизованного пользователя")

		chain {
			on { dept.parentId == deptId }
			worker("Возвращаем список из одного отдела") {
				depts = listOf(dept)
			}
			finishOperation()
		}

		chain {
			on { dept.id == deptId }
			getDeptsByParentId("Получаем потомков отдела deptId")
			finishOperation()
		}

		chain {
			on { state == ContextState.RUNNING }
			worker("Возвращаем пустой список") { depts = emptyList() }
		}
	}
}