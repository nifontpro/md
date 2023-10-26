package ru.md.base_domain.biz.validate

import ru.md.base_domain.biz.proc.BaseMedalsContext
import ru.md.base_domain.biz.proc.ContextState
import ru.md.base_domain.dept.biz.errors.deptAuthError
import ru.md.base_domain.dept.biz.errors.getDeptAuthIOError
import ru.md.cor.ICorChainDsl
import ru.md.cor.worker

/**
 * Проверка, имеет ли авторизованный пользователь доступ к верхнему доступному отделу.
 * Используется только для просмотра данных!
 */
fun <T : BaseMedalsContext> ICorChainDsl<T>.validateAuthDeptTopLevelForView(title: String) = worker {
	this.title = title
	on { state == ContextState.RUNNING }

	handle {
//		return@handle
		val authUserDeptId = authUser.dept?.id ?: throw Exception()
		if (authUser.dept?.id == deptId) return@handle
		val topLevelDeptId = baseDeptService.findTopLevelDeptId(authUserDeptId)
		log.info("deptId = $deptId")
		log.info("topLevelDeptId = $topLevelDeptId")

		if (topLevelDeptId == deptId) return@handle
		if (!baseDeptService.validateDeptChild(upId = topLevelDeptId, downId = deptId)) {
			deptAuthError()
		}
	}

	except {
		getDeptAuthIOError()
	}
}
