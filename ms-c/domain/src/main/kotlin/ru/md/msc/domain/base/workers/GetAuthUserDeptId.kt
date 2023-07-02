package ru.md.msc.domain.base.workers

import ru.md.base_domain.biz.helper.errorDb
import ru.md.base_domain.biz.helper.fail
import ru.md.base_domain.biz.proc.ContextState
import ru.md.cor.ICorChainDsl
import ru.md.cor.worker
import ru.md.msc.domain.base.biz.BaseClientContext

/**
 * Возвращает id отдела аутентифицированного пользователя
 */
fun <T : BaseClientContext> ICorChainDsl<T>.getAuthUserDeptId(title: String) = worker {
	this.title = title
	on { state == ContextState.RUNNING }

	handle {
		deptId = userService.findDeptIdByUserId(userId = authUser.id)
	}

	except {
		fail(
			errorDb(
				repository = "dept",
				violationCode = "get deptId",
				description = "Ошибка получения id отдела сотрудника"
			)
		)
	}
}