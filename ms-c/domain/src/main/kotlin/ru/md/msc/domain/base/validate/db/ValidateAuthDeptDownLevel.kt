package ru.md.msc.domain.base.validate.db

import ru.md.cor.ICorChainDsl
import ru.md.cor.worker
import ru.md.msc.domain.base.biz.BaseContext
import ru.md.msc.domain.base.biz.ContextState
import ru.md.msc.domain.dept.biz.proc.deptAuthError
import ru.md.msc.domain.dept.biz.proc.getDeptAuthIOError

/**
 * Проверка, имеет ли администратор доступ к заданному отделу ниже по иерархии
 */
fun <T : BaseContext> ICorChainDsl<T>.validateAuthDeptDownLevel(title: String) = worker {
	this.title = title
	on { state == ContextState.RUNNING }
	handle {

		val auth = try {
			deptService.validateDeptLevel(upId = authUser.dept?.id ?: 0, downId = deptId)
		} catch (e: Exception) {
			getDeptAuthIOError()
			return@handle
		}

		if (!auth) {
			deptAuthError()
		}
	}
}
