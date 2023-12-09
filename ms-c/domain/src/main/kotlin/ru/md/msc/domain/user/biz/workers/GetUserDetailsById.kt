package ru.md.msc.domain.user.biz.workers

import ru.md.base_domain.biz.proc.ContextState
import ru.md.cor.ICorChainDsl
import ru.md.cor.worker
import ru.md.msc.domain.user.biz.proc.UserContext
import ru.md.base_domain.user.biz.errors.getUserError
import ru.md.base_domain.user.biz.errors.userNotFoundError

fun ICorChainDsl<UserContext>.getUserDetailsById(title: String) = worker {

	this.title = title
	on { state == ContextState.RUNNING }

	handle {
		userDetails = userService.findByIdDetails(userId = userId) ?: run {
			userNotFoundError()
			return@handle
		}
	}

	except {
		log.error(it.message)
		getUserError()
	}

}