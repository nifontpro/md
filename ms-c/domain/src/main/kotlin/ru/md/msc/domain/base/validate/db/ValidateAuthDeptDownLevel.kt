package ru.md.msc.domain.base.validate.db

import ru.md.base_domain.biz.proc.ContextState
import ru.md.cor.ICorChainDsl
import ru.md.cor.worker
import ru.md.msc.domain.base.biz.BaseClientContext
import ru.md.msc.domain.dept.biz.proc.deptAuthError
import ru.md.msc.domain.dept.biz.proc.getDeptAuthIOError

/**
 * Проверка, имеет ли авторизованный пользователь доступ к заданному отделу ниже по иерархии
 */
fun <T : BaseClientContext> ICorChainDsl<T>.validateAuthDeptDownLevel(title: String) = worker {
	this.title = title
	on { state == ContextState.RUNNING }
	handle {
		val authUserDeptId = authUser.dept?.id ?: throw Exception()
		if (!deptService.validateDeptLevel(upId = authUserDeptId, downId = deptId)) {
			deptAuthError()
		}
	}

	except {
		getDeptAuthIOError()
	}
}
