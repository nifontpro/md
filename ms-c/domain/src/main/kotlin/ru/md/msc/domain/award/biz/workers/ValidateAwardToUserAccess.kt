package ru.md.msc.domain.award.biz.workers

import ru.md.base_domain.biz.helper.errorUnauthorized
import ru.md.base_domain.biz.helper.fail
import ru.md.base_domain.biz.proc.ContextState
import ru.md.cor.ICorChainDsl
import ru.md.cor.worker
import ru.md.msc.domain.award.biz.proc.AwardContext
import ru.md.msc.domain.dept.biz.proc.getDeptAuthIOError

/**
 * Проверка доступности награждения сотрудника определенной наградой.
 * Осуществляется по дереву отделов.
 * Отдел сотрудника должен быть равен или быть наследником отдела
 * награды в дереве отделов.
 */
fun ICorChainDsl<AwardContext>.validateAwardToUserAccess(title: String) = worker {

	this.title = title
	on { state == ContextState.RUNNING }

	handle {
		// deptId - отдел награды
		if (deptId == user.dept?.id) return@handle

		val auth = try {
			deptService.validateDeptChild(upId = deptId, downId = user.dept?.id ?: 0)
		} catch (e: Exception) {
			getDeptAuthIOError()
			return@handle
		}

		if (!auth) {
			fail(
				errorUnauthorized(
					role = "dept",
					message = "Отдел сотрудника должен быть тот же, что и у награды, или ниже по иерархии",
				)
			)
		}

	}
}