package ru.md.msc.domain.base.workers

import ru.md.base_domain.biz.helper.errorDb
import ru.md.base_domain.biz.helper.fail
import ru.md.base_domain.biz.proc.ContextState
import ru.md.cor.ICorChainDsl
import ru.md.cor.worker
import ru.md.msc.domain.base.biz.BaseClientContext

/**
 * Возвращает id родительского отдела аутентифицированного пользователя
 */
fun <T : BaseClientContext> ICorChainDsl<T>.getAuthUserParentDeptId(title: String) = worker {
	this.title = title
	on { state == ContextState.RUNNING }

	handle {
		deptId = userService.getParentDeptId(userId = authUser.id)
	}

	except {
		fail(
			errorDb(
				repository = "dept",
				violationCode = "get parent deptId",
				description = "Ошибка получения id родительского отдела"
			)
		)
	}
}