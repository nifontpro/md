package ru.md.msc.domain.user.biz.workers

import ru.md.cor.ICorChainDsl
import ru.md.cor.worker
import ru.md.msc.domain.base.biz.ContextState
import ru.md.msc.domain.base.helper.errorDb
import ru.md.msc.domain.base.helper.fail
import ru.md.msc.domain.user.biz.proc.UserContext

fun ICorChainDsl<UserContext>.updateUser(title: String) = worker {

	this.title = title
	on { state == ContextState.RUNNING }

	handle {

		try {
			userDetails = userService.update(userDetails = userDetails, isAuthUserHasAdminRole = isAuthUserHasAdminRole)
		} catch (e: Exception) {
			fail(
				errorDb(
					repository = "user",
					violationCode = "create",
					description = "Ошибка обновления профиля сотрудника"
				)
			)
		}
	}
}