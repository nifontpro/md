package ru.md.msc.domain.award.biz.workers

import ru.md.cor.ICorChainDsl
import ru.md.cor.worker
import ru.md.msc.domain.award.biz.proc.AwardContext
import ru.md.msc.domain.base.biz.ContextState
import ru.md.msc.domain.base.helper.errorUnauthorized
import ru.md.msc.domain.base.helper.fail
import ru.md.msc.domain.dept.biz.proc.getDeptAuthIOError
import ru.md.msc.domain.user.biz.proc.UserNotFoundException
import ru.md.msc.domain.user.biz.proc.getUserError
import ru.md.msc.domain.user.biz.proc.userNotFoundError

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

		userDeptId = try {
			userService.findDeptIdByUserId(userId = userId)
		} catch (e: UserNotFoundException) {
			userNotFoundError()
			return@handle
		} catch (e: Exception) {
			getUserError()
			return@handle
		}

		// deptId - отдел награды
		if (deptId == userDeptId) return@handle

		val auth = try {
			deptService.validateDeptLevel(upId = deptId, downId = userDeptId)
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