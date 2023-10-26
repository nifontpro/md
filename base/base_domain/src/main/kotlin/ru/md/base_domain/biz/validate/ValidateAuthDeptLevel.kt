package ru.md.base_domain.biz.validate

import ru.md.base_domain.biz.proc.BaseMedalsContext
import ru.md.base_domain.biz.proc.ContextState
import ru.md.base_domain.dept.biz.errors.deptAuthError
import ru.md.base_domain.dept.biz.errors.getDeptAuthIOError
import ru.md.cor.ICorChainDsl
import ru.md.cor.worker

/**
 * Проверка, имеет ли авторизованный пользователь
 * доступ к заданному отделу (включая его отдел и ниже по иерархии)
 */
fun <T : BaseMedalsContext> ICorChainDsl<T>.validateAuthDeptLevel(title: String) = worker {
	this.title = title
	on { state == ContextState.RUNNING }

	// Access top level
	handle {
		val authUserDeptId = authUser.dept?.id ?: throw Exception()
		if (authUser.dept?.id == deptId) return@handle
		val topLevelDeptId = baseDeptService.findTopLevelDeptId(authUserDeptId)
		if (topLevelDeptId == deptId) return@handle
		if (!baseDeptService.validateDeptChild(upId = topLevelDeptId, downId = deptId)) {
			deptAuthError()
		}
	}

//	handle {
//		val authUserDeptId = authUser.dept?.id ?: throw Exception()
//		if (authUserDeptId == deptId) return@handle
//
//		if (!deptService.validateDeptChild(upId = authUserDeptId, downId = deptId)) {
//			deptAuthError()
//		}
//	}

	except {
		getDeptAuthIOError()
	}
}
