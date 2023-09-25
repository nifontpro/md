package ru.md.msc.domain.award.biz.workers

import ru.md.base_domain.biz.proc.ContextState
import ru.md.cor.ICorChainDsl
import ru.md.cor.worker
import ru.md.msc.domain.award.biz.proc.AwardContext
import ru.md.msc.domain.user.biz.proc.getUserError
import ru.md.msc.domain.user.biz.proc.userNotFoundError

fun ICorChainDsl<AwardContext>.getUserById(title: String) = worker {

	this.title = title
	on { state == ContextState.RUNNING }

	handle {
		user = userService.findById(userId) ?: run {
			userNotFoundError()
			return@handle
		}
	}

	except {
		getUserError()
	}
}