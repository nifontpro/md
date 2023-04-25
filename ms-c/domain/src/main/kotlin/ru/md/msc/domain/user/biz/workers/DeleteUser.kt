package ru.md.msc.domain.user.biz.workers

import ru.md.cor.ICorChainDsl
import ru.md.cor.worker
import ru.md.msc.domain.base.biz.ContextState
import ru.md.msc.domain.base.helper.errorDb
import ru.md.msc.domain.base.helper.fail
import ru.md.msc.domain.user.biz.proc.UserContext

fun ICorChainDsl<UserContext>.deleteUser(title: String) = worker {

	this.title = title
	on { state == ContextState.RUNNING }

	handle {

		try {
			userService.deleteById(userId)
		} catch (e: Exception) {
			log.error(e.message)
			fail(
				errorDb(
					repository = "user",
					violationCode = "delete",
					description = "Ошибка удаления сотрудника"
				)
			)
		}
	}
}