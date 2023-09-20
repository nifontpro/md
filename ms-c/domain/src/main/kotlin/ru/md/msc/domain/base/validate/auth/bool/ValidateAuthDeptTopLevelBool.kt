package ru.md.msc.domain.base.validate.auth.bool

import ru.md.base_domain.biz.proc.ContextState
import ru.md.cor.ICorChainDsl
import ru.md.cor.worker
import ru.md.msc.domain.base.biz.BaseClientContext
import ru.md.msc.domain.dept.biz.proc.getDeptAuthIOError

/**
 * Проверка, имеет ли авторизованный пользователь доступ к верхнему доступному отделу.
 * Используется только для просмотра данных!
 * Возвращает isAuth
 */
fun <T : BaseClientContext> ICorChainDsl<T>.validateAuthDeptTopLevelForViewBool(title: String) = worker {
	this.title = title
	on { state == ContextState.RUNNING }

	handle {
		val authUserDeptId = authUser.dept?.id ?: throw Exception()
		if (authUser.dept?.id == deptId) {
			isAuth = true
			return@handle
		}
		val topLevelDeptId = deptService.findTopLevelDeptId(authUserDeptId)
		isAuth = deptService.validateDeptChild(upId = topLevelDeptId, downId = deptId)
	}

	except {
		getDeptAuthIOError()
	}
}
