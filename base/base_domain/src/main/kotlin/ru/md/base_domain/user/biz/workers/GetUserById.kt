package ru.md.base_domain.user.biz.workers

import ru.md.base_domain.biz.proc.BaseMedalsContext
import ru.md.base_domain.biz.proc.ContextState
import ru.md.cor.ICorChainDsl
import ru.md.cor.worker
import ru.md.base_domain.user.biz.errors.getUserError
import ru.md.base_domain.user.biz.errors.userNotFoundError

fun <T: BaseMedalsContext> ICorChainDsl<T>.getUserById(title: String) = worker {

	this.title = title
	on { state == ContextState.RUNNING }

	handle {
		user = baseUserService.findById(userId) ?: run {
			userNotFoundError()
			return@handle
		}
	}

	except {
		getUserError()
	}
}