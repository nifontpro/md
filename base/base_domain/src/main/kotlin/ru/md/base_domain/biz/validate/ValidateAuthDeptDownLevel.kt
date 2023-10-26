package ru.md.base_domain.biz.validate

import ru.md.base_domain.biz.proc.BaseMedalsContext
import ru.md.base_domain.biz.proc.ContextState
import ru.md.cor.ICorChainDsl
import ru.md.cor.worker
import ru.md.base_domain.dept.biz.errors.deptAuthError
import ru.md.base_domain.dept.biz.errors.getDeptAuthIOError

/**
 * Проверка, имеет ли авторизованный пользователь доступ к заданному отделу ниже по иерархии
 */
fun <T : BaseMedalsContext> ICorChainDsl<T>.validateAuthDeptDownLevel(title: String) = worker {
	this.title = title
	on { state == ContextState.RUNNING }
	handle {
		val authUserDeptId = authUser.dept?.id ?: throw Exception()
		if (!baseDeptService.validateDeptChild(upId = authUserDeptId, downId = deptId)) {
			deptAuthError()
		}
	}

	except {
		getDeptAuthIOError()
	}
}
