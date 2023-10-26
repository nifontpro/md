package ru.md.base_domain.biz.validate

import ru.md.base_domain.biz.proc.BaseMedalsContext
import ru.md.base_domain.biz.proc.ContextState
import ru.md.base_domain.dept.biz.errors.getDeptAuthIOError
import ru.md.cor.ICorChainDsl
import ru.md.cor.worker

/**
 * Проверка, имеет ли авторизованный пользователь доступ к верхнему доступному отделу.
 * Используется только для просмотра данных!
 * Возвращает isAuth
 */
fun <T : BaseMedalsContext> ICorChainDsl<T>.validateAuthDeptTopLevelForViewBool(title: String) = worker {
	this.title = title
	on { state == ContextState.RUNNING }

	handle {
		val authUserDeptId = authUser.dept?.id ?: throw Exception()
		if (authUser.dept?.id == deptId) {
			isAuth = true
			return@handle
		}
		val topLevelDeptId = baseDeptService.findTopLevelDeptId(authUserDeptId)
		isAuth = baseDeptService.validateDeptChild(upId = topLevelDeptId, downId = deptId)
	}

	except {
		getDeptAuthIOError()
	}
}
