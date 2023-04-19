package ru.md.msc.domain.user.biz.workers

import ru.md.cor.ICorChainDsl
import ru.md.cor.worker
import ru.md.msc.domain.base.biz.ContextState
import ru.md.msc.domain.user.biz.proc.getUserError
import ru.md.msc.domain.user.biz.proc.UserContext

fun ICorChainDsl<UserContext>.getProfiles(title: String) = worker {

	this.title = title
	on { state == ContextState.RUNNING }

	handle {

		users = try {
			userService.findByAuthEmailWithDept(authEmail = authEmail)
		} catch (e: Exception) {
			getUserError()
			return@handle
		}
	}
}