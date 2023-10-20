package ru.md.msc.domain.user.biz.workers

import ru.md.base_domain.biz.proc.ContextState
import ru.md.cor.ICorChainDsl
import ru.md.cor.worker
import ru.md.msc.domain.user.biz.proc.UserContext
import ru.md.msc.domain.user.biz.proc.userCreateError

fun ICorChainDsl<UserContext>.createUser(title: String) = worker {

	this.title = title
	on { state == ContextState.RUNNING }

	handle {
		userDetails = userService.create(userDetails = userDetails)
	}

	except {
		log.error(it.message)
		userCreateError()
	}
}