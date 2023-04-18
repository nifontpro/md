package ru.md.msc.domain.user.biz.workers

import ru.md.cor.ICorChainDsl
import ru.md.cor.worker
import ru.md.msc.domain.base.biz.ContextState
import ru.md.msc.domain.base.model.checkRepositoryData
import ru.md.msc.domain.user.biz.proc.UserContext

fun ICorChainDsl<UserContext>.getUserDetailsById(title: String) = worker {

	this.title = title
	on { state == ContextState.RUNNING }

	handle {

		userDetails = checkRepositoryData {
			userService.findByIdDetails(userId = userId)
		} ?: return@handle
	}
}